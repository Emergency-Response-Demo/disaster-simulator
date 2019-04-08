package com.redhat.cajun.navy.datagenerate;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;


public class SendIncident extends AbstractVerticle {

    HttpClient httpClient = null;
    String incidentService = "/incident";
    String host = "http://incident-service-naps-emergency-response.apps.753d.openshift.opentlc.com";

    @Override
    public void init(Vertx vertx, Context context) {
        vertx.eventBus().consumer("incident-queue", this::onMessage);
        httpClient = vertx.createHttpClient();

    }


    public enum ErrorCodes {
        NO_ACTION_SPECIFIED,
        BAD_ACTION
    }


    public void onMessage(Message<JsonObject> message) {

        String action = message.headers().get("action");
        switch (action) {
            case "send-incident":
                System.out.println("incident recieved");
                Victim v = Json.decodeValue(String.valueOf(message.body()), Victim.class);
                System.out.println("incident recieved"+v.toString());
                message.reply("Incident recieved"+v);


                sendRequest(incidentService, v);

                break;

            default:
                message.fail(ErrorCodes.BAD_ACTION.ordinal(),"Message send-incident failed");
        }
    }


    private void sendRequest(String uri, Victim v) {
        System.out.println(host + uri);
        httpClient.post(host, uri, response -> {
            if (response.statusCode() != 200) {
                response.bodyHandler(b -> System.out.println(b.toString()));
                System.err.println(response.statusCode() + " " + response.statusMessage());
            } else {
                response.bodyHandler(b -> System.out.println(b.toString()));
            }
        }).putHeader("content-type", "application/json").end();

    }


}
