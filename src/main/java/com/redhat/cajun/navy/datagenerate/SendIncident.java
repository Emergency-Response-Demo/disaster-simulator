package com.redhat.cajun.navy.datagenerate;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;


public class SendIncident extends AbstractVerticle {

    String uri = "/incident";
    String host = "incident-service-naps-emergency-response.apps.753d.openshift.opentlc.com";
    WebClient client = null;
    boolean isEnabled = false;
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        System.out.println(config());
        vertx.eventBus().consumer("incident-queue", this::onMessage);
        client = WebClient.create(vertx);
        host = config().getString("incident.service");
        uri = config().getString("incident.uri");
        isEnabled = config().getBoolean("enabled");
        System.out.println("IncidentService located at: "+host + uri);

    }

    public enum ErrorCodes {
        NO_ACTION_SPECIFIED,
        BAD_ACTION
    }


    public void onMessage(Message<JsonObject> message) {

        String action = message.headers().get("action");
        switch (action) {
            case "send-incident":

                Victim v = Json.decodeValue(String.valueOf(message.body()), Victim.class);
                message.reply("Incident recieved"+v);

                sendRequest(v);

                break;

            default:
                message.fail(ErrorCodes.BAD_ACTION.ordinal(),"Message send-incident failed");
        }
    }


    private void sendRequest(Victim v) {

        System.out.println(host + uri + v);
        if(isEnabled){
                    client
                .post(80, host, uri)
                .sendJsonObject(JsonObject.mapFrom(v), ar -> {
                            if (ar.succeeded()) {
                                System.out.println("incident sent "+v.toString());
                            }
                            else System.out.println("incident send request failed "+v.toString());
                        });


        }
    }


}
