package com.redhat.cajun.navy.datagenerate;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import rx.Observable;

import java.util.HashSet;
import java.util.List;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

public class HttpApplication extends AbstractVerticle {

    private static Disaster disaster = null;

    private HashSet<Victim> victims = new HashSet<Victim>();

    private int victimCount = 0;

    @Override
    public void start(Future<Void> future) {
        System.out.println(config());
        disaster = new Disaster(config().getString("fnames.file"), config().getString("lnames.file"));

        // Create a router object.
        Router router = Router.router(vertx);

        router.get("/api/generate").handler(this::generate);
        router.get("/api/lastrun").handler(this::lastrun);
        router.get("/*").handler(StaticHandler.create());

        // Create the HTTP server and pass the "accept" method to the request handler.
        vertx
                .createHttpServer()
                .requestHandler(router)
                .listen(
                        // Retrieve the port from the configuration, default to 8080.
                        config().getInteger("http.port", 8080), ar -> {
                            if (ar.succeeded()) {
                                System.out.println("Server started on port " + ar.result().actualPort());
                            }
                            future.handle(ar.mapEmpty());
                        });
    }

    private void lastrun(RoutingContext rc) {
        rc.response()
                .putHeader(CONTENT_TYPE, "application/json; charset=utf-8")
                .end(Json.encodePrettily(victims));
    }

    private void generate(RoutingContext rc) {
        victims.clear();

        String numVictimsStr = rc.request().getParam("numVictims");
        if (numVictimsStr == null) {
            numVictimsStr = "10";
        }

        String waitTimeStr = rc.request().getParam("waitTime");
        if (waitTimeStr == null) {
            waitTimeStr = "1000";
        }

        victimCount = 0;

        try {
            int numVictims = Integer.parseInt(numVictimsStr);
            final int waitTime = Integer.parseInt(waitTimeStr);
            Observable<Victim> ob = Observable.from(disaster.generateVictims(numVictims));
            ob.subscribe(
                item -> {victimCount++; sendMessage(item, victimCount, waitTime);}, 
                error -> error.printStackTrace(),
                () -> System.out.println("Done"));


        }catch(NumberFormatException nfe){
            nfe.printStackTrace();
        }

        JsonObject response = new JsonObject()
                .put("content/json", "If enabled, requests are sent to incident service, check logs for more details on the requests or endpoint [api/lastrun]");

        rc.response()
                .putHeader(CONTENT_TYPE, "application/json; charset=utf-8")
                .end(response.encodePrettily());
    }

    public void sendMessage(Victim victim, int victimCount, int delay) {
        int waitTime = victimCount * delay;
        // Cannot schedule a timer with delay < 1 ms, so if waitTime is 0, set it to 1.
        if(waitTime == 0) {
            waitTime = 1;
        } 
        final int calculatedWaitTime = waitTime;
        System.out.format("send Message Called for victim %d with a delay of %d\n", victimCount, calculatedWaitTime);
        vertx.setTimer(waitTime, new Handler<Long>() {
            public void handle(Long timerID) {
                System.out.format("Sending victim %d after delay of %d milliseconds\n", victimCount, calculatedWaitTime); 

                DeliveryOptions options = new DeliveryOptions().addHeader("action", "send-incident");
                vertx.eventBus().send("incident-queue", victim.toString(), options, reply -> {
                    if (reply.succeeded()) {
                        System.out.format("Message accepted for victim %d\n", victimCount);
                        victims.add(victim);
                    } else {
                        System.out.format("Message NOT accepted for victim %d\n", victimCount);
                    }
                });
        
            }
        });

    }

}
