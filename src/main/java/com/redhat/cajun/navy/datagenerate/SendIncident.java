package com.redhat.cajun.navy.datagenerate;

import io.vertx.core.*;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

public class SendIncident extends AbstractVerticle {

    HttpClient httpClient = null;

    @Override
    public void init(Vertx vertx, Context context) {
        vertx.eventBus().consumer("incident-queue", this::onMessage);
    }

    @Override
    public void start() throws Exception {
        //httpClient = vertx.createHttpClient();
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
                break;
            default:
                message.fail(ErrorCodes.BAD_ACTION.ordinal(),"Message send-incident failed");
        }
    }


}
