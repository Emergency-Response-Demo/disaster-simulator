package com.redhat.cajun.navy.datagenerate;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

import java.util.HashSet;
import java.util.List;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import rx.Observable;

public class HttpApplication extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(HttpApplication.class);

    private static Disaster disaster = null;

    private HashSet<Victim> victims = new HashSet<Victim>();

    private int victimCount = 0;

    @Override
    public void start(Future<Void> future) {
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
                                log.info("Server started on port " + ar.result().actualPort());
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

        String incidentsStr = rc.request().getParam("incidents");
        if (incidentsStr == null) {
            incidentsStr = "10";
        }

        String waitTimeStr = rc.request().getParam("waitTime");
        if (waitTimeStr == null) {
            waitTimeStr = "1000";
        }

        String respondersStr = rc.request().getParam("responders");
        if (respondersStr == null) {
            respondersStr = "25";
        }

        victimCount = 0;

        try {
            int numVictims = Integer.parseInt(incidentsStr);
            final int waitTime = Integer.parseInt(waitTimeStr);
            int numResponders = Integer.parseInt(respondersStr);
            if (numResponders > 150) {
                numResponders = 150;
            }

            sendResponderResetMessage();

            List<Responder> responders = disaster.generateResponders(numResponders);
            sendResponderInitMessage(responders);

            Observable<Victim> ob = Observable.from(disaster.generateVictims(numVictims));
            ob.subscribe(
                item -> {victimCount++; sendMessage(item, victimCount, waitTime);},
                error -> log.error("Error handling victims", error));


        } catch(NumberFormatException nfe){
            log.error("Number Format exception", nfe);
        }

        JsonObject response = new JsonObject()
                .put("content/json", "If enabled, requests are sent to incident service, check logs for more details on the requests or endpoint [api/lastrun]");

        rc.response()
                .putHeader(CONTENT_TYPE, "application/json; charset=utf-8")
                .end(response.encodePrettily());
    }

    private void sendResponderResetMessage() {
        DeliveryOptions options = new DeliveryOptions().addHeader("action", "reset-responders");
        vertx.eventBus().send("responder-queue", new JsonObject(), options);
    }

    private void sendResponderInitMessage(List<Responder> responders) {
        DeliveryOptions options = new DeliveryOptions().addHeader("action", "init-responders");
        vertx.eventBus().send("responder-queue", new JsonObject().put("responders", new JsonArray(Json.encode(responders))), options);
    }

    private void sendMessage(Victim victim, int victimCount, int delay) {
        int waitTime = victimCount * delay;
        // Cannot schedule a timer with delay < 1 ms, so if waitTime is 0, set it to 1.
        if(waitTime == 0) {
            waitTime = 1;
        }
        final int calculatedWaitTime = waitTime;
        log.info("send Message Called for victim " + victimCount + " with a delay of " + calculatedWaitTime + " milliseconds");
        vertx.setTimer(waitTime, new Handler<Long>() {
            public void handle(Long timerID) {
                log.info("Sending victim " + victimCount + " after delay of " + calculatedWaitTime + " milliseconds");

                DeliveryOptions options = new DeliveryOptions().addHeader("action", "send-incident");
                vertx.eventBus().send("incident-queue", victim.toString(), options, reply -> {
                    if (reply.succeeded()) {
                        log.info("Message accepted for victim " + victimCount);
                        victims.add(victim);
                    } else {
                        log.warn("Message NOT accepted for victim " + victimCount);
                    }
                });
        
            }
        });

    }

}
