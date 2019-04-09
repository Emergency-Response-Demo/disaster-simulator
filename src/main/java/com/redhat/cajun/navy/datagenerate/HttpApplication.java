package com.redhat.cajun.navy.datagenerate;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import rx.Observable;

import java.util.List;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

public class HttpApplication extends AbstractVerticle {

    private static Disaster disaster = null;

    @Override
    public void start(Future<Void> future) {
        System.out.println(config());
        disaster = new Disaster(config().getString("fnames.file"), config().getString("lnames.file"));

        // Create a router object.
        Router router = Router.router(vertx);

        router.get("/api/generate").handler(this::generate);
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

    private void generate(RoutingContext rc) {

        String name = rc.request().getParam("name");
        if (name == null) {
            name = "10";
        }

        List<Victim> list = null;

        try {
            int n = Integer.parseInt(name);
            Observable<Victim> ob = Observable.from(disaster.generateVictims(n));
            ob.subscribe(item -> sendMessage(item), error -> error.printStackTrace(),
                    () -> System.out.println("Done"));


        }catch(NumberFormatException nfe){
            nfe.printStackTrace();
        }

        JsonObject response = new JsonObject()
                .put("content/json", "If enabled, requests are sent to incident service, check logs for more details on the requests");

        rc.response()
                .putHeader(CONTENT_TYPE, "application/json; charset=utf-8")
                .end(response.encodePrettily());
    }

    public void sendMessage(Victim victim) {
        DeliveryOptions options = new DeliveryOptions().addHeader("action", "send-incident");
        vertx.eventBus().send("incident-queue", victim.toString(), options, reply -> {
            if (reply.succeeded()) {
                System.out.println("Message accepted");
            } else {
                System.out.println("Message not accepted");
            }
        });

    }

}
