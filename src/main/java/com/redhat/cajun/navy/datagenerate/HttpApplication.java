package com.redhat.cajun.navy.datagenerate;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
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
                            future.complete();
                        });

    }

    private void lastrun(RoutingContext rc) {
        rc.response()
                .putHeader(CONTENT_TYPE, "application/json; charset=utf-8")
                .end(Json.encodePrettily(victims));
    }

    private void generate(RoutingContext rc) {
        victims.clear();

        int incidents = toInteger(rc.request().getParam("incidents")).orElse(10);
        int waitTime = toInteger(rc.request().getParam("waitTime")).orElse(1000);
        int responders = toInteger(rc.request().getParam("responders")).orElse(25);
        boolean clearIncidents = Boolean.valueOf(rc.request().getParam("clearIncidents"));
        boolean clearMissions = Boolean.valueOf(rc.request().getParam("clearMissions"));

        log.info("Simulator called with incidents = " + incidents + ", wait time = " + waitTime + ", responders = "
                + responders + ", clear incidents = " + clearIncidents + ", clear missions = " + clearMissions);

        victimCount = 0;

        // Reset responders
        List<Responder> responderList = disaster.generateResponders(responders);
        DeliveryOptions options = new DeliveryOptions().addHeader("action", "reset-responders");
        vertx.eventBus().send("rest-client-queue", new JsonObject().put("responders", new JsonArray(Json.encode(responderList))), options);

        // Reset incidents
        if (clearIncidents) {
            options = new DeliveryOptions().addHeader("action", "reset-incidents");
            vertx.eventBus().send("rest-client-queue", new JsonObject(), options);
        }

        // Reset Incident Priority
        options = new DeliveryOptions().addHeader("action", "reset-incident-priority");
        vertx.eventBus().send("rest-client-queue", new JsonObject(), options);

        // Reset missions
        if (clearMissions) {
            options = new DeliveryOptions().addHeader("action", "reset-missions");
            vertx.eventBus().send("rest-client-queue", new JsonObject(), options);
        }

        // Create Incidents
        Observable<Victim> ob = Observable.from(disaster.generateVictims(incidents));
        ob.subscribe(
                item -> {victimCount++; sendMessage(item, victimCount, waitTime);},
                error -> log.error("Error handling victims", error));

        JsonObject response = new JsonObject()
                .put("content/json", "If enabled, requests are sent to incident service, check logs for more details on the requests or endpoint [api/lastrun]");

        rc.response()
                .putHeader(CONTENT_TYPE, "application/json; charset=utf-8")
                .end(response.encodePrettily());
    }

    private void sendMessage(Victim victim, int victimCount, int delay) {
        int waitTime = victimCount * delay;
        // Cannot schedule a timer with delay < 1 ms, so if waitTime is 0, set it to 1.
        if(waitTime == 0) {
            waitTime = 1;
        }
        final int calculatedWaitTime = waitTime;
        log.info("Send message called for victim " + victimCount + " with a delay of " + calculatedWaitTime + " milliseconds");
        vertx.setTimer(waitTime, timerID -> {
            log.info("Sending victim " + victimCount + " after delay of " + calculatedWaitTime + " milliseconds");

            DeliveryOptions options = new DeliveryOptions().addHeader("action", "create-incident");
            vertx.eventBus().send("rest-client-queue", new JsonObject(Json.encode(victim)), options);
        });

    }

    private Optional<Integer> toInteger(String intStr) {
        if (intStr == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(intStr));
        } catch (NumberFormatException nfe) {
            return Optional.empty();
        }
    }

}
