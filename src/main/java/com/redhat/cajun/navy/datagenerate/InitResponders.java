package com.redhat.cajun.navy.datagenerate;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;


public class InitResponders extends AbstractVerticle {

    private final static Logger log = LoggerFactory.getLogger(InitResponders.class);

    private WebClient client = null;
    private String host;
    private String initPath;
    private String resetPath;
    private Integer port;

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        vertx.eventBus().consumer("responder-queue", this::onMessage);
        client = WebClient.create(vertx);
        host = config().getString("responder.service");
        initPath = config().getString("responder.path.init");
        resetPath = config().getString("responder.path.reset");
        port = config().getInteger("responder.port");
    }

    public enum ErrorCodes {
        NO_ACTION_SPECIFIED,
        BAD_ACTION
    }


    public void onMessage(Message<JsonObject> message) {

        String action = message.headers().get("action");
        switch (action) {
            case "init-responders":
                sendInitRespondersRequest(message.body().getJsonArray("responders"));
                break;

            case "reset-responders":
                sendResetRespondersRequest();
                break;

            default:
                message.fail(ErrorCodes.BAD_ACTION.ordinal(),"Message send-incident failed");
        }
    }

    private void sendResetRespondersRequest() {
        client.post(port, host, resetPath).send(ar -> {
            if (ar.succeeded()) {
                HttpResponse<Buffer> response = ar.result();
                log.info("Reset responders: HTTP response status " + response.statusCode());
            } else {
                log.error("Reset responders failed.", ar.cause());
            }
        });
    }

    private void sendInitRespondersRequest(JsonArray json) {
        client.post(port, host, initPath).putHeader("Content-Type", "application/json").sendBuffer(json.toBuffer(), ar -> {
            if (ar.succeeded()) {
                HttpResponse<Buffer> response = ar.result();
                log.info("Init responders: HTTP response status " + response.statusCode());
            } else {
                log.error("Init responders failed.", ar.cause());
            }
        });
    }
}
