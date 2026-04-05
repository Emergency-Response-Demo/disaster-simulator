package com.redhat.cajun.navy.datagenerate;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;


public class MainVerticle extends AbstractVerticle {

    private static Logger log = LoggerFactory.getLogger(MainVerticle.class);

    @Override
    public void start(final Future<Void> future) {

        ConfigRetriever.create(vertx, selectConfigOptions())
                .getConfig(ar -> {
                    if (ar.succeeded()) {
                        deployVerticles(ar.result(), future);
                    } else {
                        log.warn("Failed to retrieve the configuration.");
                        future.fail(ar.cause());
                    }
                });
    }


    private ConfigRetrieverOptions selectConfigOptions(){
        ConfigRetrieverOptions options = new ConfigRetrieverOptions();

        ConfigStoreOptions defaults = new ConfigStoreOptions()
                .setType("json")
                .setConfig(new JsonObject()
                        .put("fnames.file", "fnames.txt")
                        .put("lnames.file", "lnames.txt")
                        .put("http.port", 8080)
                        .put("is.dryrun", false)
                        .put("disaster.service.host", "localhost")
                        .put("disaster.service.port", 8080)
                        .put("disaster.service.path.inclusion.zones", "/inclusion-zones")
                        .put("incident.service.host", "localhost")
                        .put("incident.service.port", 8080)
                        .put("incident.service.path.reset", "/reset")
                        .put("incident.service.path.create", "/create")
                        .put("responder.service.host", "localhost")
                        .put("responder.service.port", 8080)
                        .put("responder.service.path.reset", "/reset")
                        .put("responder.service.path.clear", "/clear")
                        .put("responder.service.path.create", "/create")
                        .put("mission.service.host", "localhost")
                        .put("mission.service.port", 8080)
                        .put("mission.service.path.reset", "/reset")
                        .put("incidentpriority.service.host", "localhost")
                        .put("incidentpriority.service.port", 8080)
                        .put("incidentpriority.service.path.reset", "/reset")
                        .put("process.service.host", "localhost")
                        .put("process.service.port", 8080)
                        .put("process.service.path.reset", "/reset")
                        .put("responder.simulator.host", "localhost")
                        .put("responder.simulator.port", 8080)
                        .put("responder.simulator.path.reset", "/reset")
                        .put("simulation.minPeople", 1)
                        .put("simulation.maxPeople", 10)
                        .put("simulation.peopleBias", 1.3)
                        .put("simulation.medicalNeededProb", 0.5)
                        .put("simulation.minBoatCapacity", 1)
                        .put("simulation.maxBoatCapacity", 12)
                        .put("simulation.boatCapacityBias", 0.5)
                        .put("simulation.medicalKitProb", 0.5)
                        .put("simulation.prob.pregnant", 0.1)
                        .put("simulation.prob.conscious", 0.9)
                        .put("simulation.prob.mobility.ambulatory", 0.7)
                        .put("simulation.prob.mobility.assisted", 0.2)
                        .put("simulation.prob.responder.boat", 0.8)
                        .put("simulation.prob.responder.helicopter", 0.05)
                        .put("simulation.scraped.data.file", "")
                        .put("simulation.geojson.file", "")
                );
        options.addStore(defaults);

        if (System.getenv("KUBERNETES_NAMESPACE") != null) {
            ConfigStoreOptions appStore = new ConfigStoreOptions()
                    .setType("file")
                    .setFormat("properties")
                    .setConfig(new JsonObject()
                            .put("name", System.getenv("APP_CONFIGMAP_NAME"))
                            .put("key", System.getenv("APP_CONFIGMAP_KEY"))
                            .put("path", "/deployments/config/app-config.properties"));
            options.addStore(appStore);
        } else {
            if (System.getProperty("vertx-config-path") != null) {
                ConfigStoreOptions props = new ConfigStoreOptions()
                        .setType("file")
                        .setFormat("properties")
                        .setConfig(new JsonObject().put("path", System.getProperty("vertx-config-path")));
                options.addStore(props);
            }
        }

        return options;
    }


    private void deployVerticles(JsonObject config, Future<Void> future){

        Future<String> httpAppFuture = Future.future();
        Future<String> restClientFuture = Future.future();

        DeploymentOptions options = new DeploymentOptions();

        options.setConfig(config);
        vertx.deployVerticle(new HttpApplication(), options, httpAppFuture);
        vertx.deployVerticle(new RestClientVerticle(), options, restClientFuture);

        CompositeFuture.all(httpAppFuture, restClientFuture).setHandler(ar -> {
            if (ar.succeeded()) {
                log.info("Verticles deployed successfully.");
                future.complete();
            } else {
                log.error("WARNINIG: Verticles NOT deployed successfully.", ar.cause());
                future.fail(ar.cause());
            }
        });

    }




    // Used for debugging in IDE
    public static void main(String[] args) {
        io.vertx.reactivex.core.Vertx vertx = io.vertx.reactivex.core.Vertx.vertx();

        vertx.rxDeployVerticle(MainVerticle.class.getName())
                .subscribe();
    }

}