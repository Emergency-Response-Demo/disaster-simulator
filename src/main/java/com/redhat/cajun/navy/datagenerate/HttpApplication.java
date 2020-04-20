package com.redhat.cajun.navy.datagenerate;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.StaticHandler;
import rx.Observable;

public class HttpApplication extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(HttpApplication.class);

    private static Disaster disaster = null;

    private HashSet<Victim> victims = new HashSet<Victim>();
    private List<Responder> respondersForLastRun = new ArrayList<>();

    private int victimCount = 0;

    private boolean isDryRun = false;

    @Override
    public void start(Future<Void> future) {
        disaster = new Disaster(config().getString("fnames.file"), config().getString("lnames.file"));
        isDryRun = config().getBoolean("is.dryrun", false);

        // Create a router object.
        Router router = Router.router(vertx);

        router.get("/g/incidents").handler(this::generateIncidents);
        router.get("/g/responders").handler(this::generateResponders);
        router.get("/c/incidents").handler(this::clearIncidents);
        router.get("/c/responders").handler(this::clearResponders);
        router.get("/c/missions").handler(this::clearMissions);

        router.get("/g/incidents/lastrun").handler(this::lastRunIncidents);
        router.get("/g/responders/lastrun").handler(this::lastRunResponders);
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
        if(isDryRun)
            log.info("CHECK ConfigMap: running in Dry Run mode!! is.dryrun="+isDryRun);

    }

    private void lastRunIncidents(RoutingContext rc) {
        rc.response()
                .putHeader(CONTENT_TYPE, "application/json; charset=utf-8")
                .end(Json.encodePrettily(victims));
    }

    private void lastRunResponders(RoutingContext rc) {
        rc.response()
                .putHeader(CONTENT_TYPE, "application/json; charset=utf-8")
                .end(Json.encodePrettily(respondersForLastRun));
    }


    private void clearMissions(RoutingContext routingContext){
        if(!isDryRun) {
            boolean clearMissions = Boolean.valueOf(routingContext.request().getParam("clearMissions"));
            // Reset missions
            if (clearMissions) {
                DeliveryOptions options = new DeliveryOptions().addHeader("action", "reset-missions");
                vertx.eventBus().send("rest-client-queue", new JsonObject(), options);

                options = new DeliveryOptions().addHeader("action", "abort-process-instances");
                vertx.eventBus().send("rest-client-queue", new JsonObject(), options);
            }
        }
        JsonObject response = new JsonObject()
                .put("response", "Requests sent to mission service, it will complete all missions and clear the cache. check logs for more details..")
                .put("isDryRun", isDryRun);

        routingContext.response()
                .putHeader(CONTENT_TYPE, "application/json; charset=utf-8")
                .end(response.encodePrettily());

    }

    private void clearIncidents(RoutingContext routingContext){
        if(!isDryRun) {
            boolean clearIncidents = Boolean.valueOf(routingContext.request().getParam("clearIncidents"));
            // Reset incidents
            if (clearIncidents) {
                DeliveryOptions options = new DeliveryOptions().addHeader("action", "reset-incidents");
                vertx.eventBus().send("rest-client-queue", new JsonObject(), options);

                // Reset Incident Priority
                options = new DeliveryOptions().addHeader("action", "reset-incident-priority");
                vertx.eventBus().send("rest-client-queue", new JsonObject(), options);
            }
        }
        JsonObject response = new JsonObject()
                .put("response", "Requests sent to incident service, check logs for more details..")
                .put("isDryRun", isDryRun);

        routingContext.response()
                .putHeader(CONTENT_TYPE, "application/json; charset=utf-8")
                .end(response.encodePrettily());

    }

    private void clearResponders(RoutingContext routingContext){
        if(!isDryRun) {
            boolean clearResponders = Boolean.valueOf(routingContext.request().getParam("clearResponders"));
            // Reset incidents
            if (clearResponders) {
                DeliveryOptions options = new DeliveryOptions().addHeader("action", "clear-responders");
                vertx.eventBus().send("rest-client-queue", new JsonObject(), options);
            }
        }
        JsonObject response = new JsonObject()
                .put("response", "Clear request sent to Responder Service, check logs for more details..")
                .put("isDryRun", isDryRun);

        routingContext.response()
                .putHeader(CONTENT_TYPE, "application/json; charset=utf-8")
                .end(response.encodePrettily());

    }

    private void generateResponders(RoutingContext routingContext) {
        int responders = toInteger(routingContext.request().getParam("responders")).orElse(25);
        boolean resetResponders = Boolean.valueOf(routingContext.request().getParam("resetResponders"));

        respondersForLastRun = new ArrayList<>();

        if(!isDryRun){
            if (resetResponders) {
                DeliveryOptions options = new DeliveryOptions().addHeader("action", "reset-responders");
                vertx.eventBus().send("rest-client-queue", new JsonObject(), options);
            }
            Future<String> polygonUpdate = updateBoundingPolygons();
            polygonUpdate.setHandler(result -> {
                List<Responder> responderList = disaster.generateResponders(responders);
                respondersForLastRun.addAll(responderList);

                DeliveryOptions options = new DeliveryOptions().addHeader("action", "create-responders");
                vertx.eventBus().send("rest-client-queue", new JsonObject().put("responders", new JsonArray(Json.encode(responderList))), options);
            });
        }

        JsonObject response = new JsonObject()
                .put("response", "Requests sent to responder service, check logs for more details on the requests or invoke the endpoint [/g/responders/lastrun] ")
                .put("isDryRun", isDryRun);

        routingContext.response()
                .putHeader(CONTENT_TYPE, "application/json; charset=utf-8")
                .end(response.encodePrettily());
    }


    private void generateIncidents(RoutingContext rc) {
        victims.clear();

        int numVictims = toInteger(rc.request().getParam("incidents")).orElse(10);
        int waitTime = toInteger(rc.request().getParam("waitTime")).orElse(1000);

        victimCount = 0;

        Future<String> polygonUpdate = updateBoundingPolygons();
        polygonUpdate.setHandler(result -> {
            Observable<Victim> ob = Observable.from(disaster.generateVictims(numVictims));
            ob.subscribe(
                item -> {
                    victimCount++;
                    if(!isDryRun)
                        sendMessage(item, victimCount, waitTime);
                    victims.add(item);
                },
                error -> log.error("Exception while generating incidents", error),
                () -> log.info("Incidents generated"));
        });

        JsonObject response = new JsonObject()
                    .put("response", "If enabled, requests are sent to incident service, check logs for more details on the requests or endpoint [/g/incidents/lastrun]")
                    .put("isDryRun", isDryRun);
    
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

    /**
     * Fetch the current inclusion zones from the disaster service, and update the Disaster's BoundingPolygons object.
     * 
     * @return a future indicating the result
     */
    private Future<String> updateBoundingPolygons() {
        disaster.boundingPolygons.clearCurrentPolygons();
        Future<String> future = Future.future();

        //TODO: Also retrieve exclusion zones
        WebClient webClient = WebClient.create(vertx);
        webClient.get(
            config().getInteger("disaster.service.port"), 
            config().getString("disaster.service.host"), 
            config().getString("disaster.service.path.inclusion.zones")
        ).send(response -> {
            log.info("Received response from disaster service: {}", response.result().bodyAsJsonArray().encodePrettily());
            ServicePolygon polygons[] = Json.decodeValue(response.result().bodyAsString(), ServicePolygon[].class);
            for (ServicePolygon polygon : polygons) {
                Waypoint waypoints[] = new Waypoint[polygon.getPoints().size()];
                polygon.getPoints().stream()
                    .map(point -> new Waypoint(point[1], point[0]))
                    .collect(Collectors.toList())
                    .toArray(waypoints);
                log.info("Adding inclusion polygon to disaster: {}", waypoints.toString());
                disaster.boundingPolygons.setInclusionPolygon(waypoints);
            }
            future.complete();
        });

        return future;
    }
}
