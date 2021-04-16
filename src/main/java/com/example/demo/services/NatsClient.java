package com.example.demo.services;

import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;
import io.nats.client.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// https://github.com/eugenp/tutorials/raw/master/libraries-5/src/main/java/com/baeldung/jnats/NatsClient.java
@Slf4j
@Service
public class NatsClient {
    public static final String TEST_TOPIC = "TEST_TOPIC";
    private final String serverURI;
    private final Map<String, Subscription> subscriptions = new HashMap<>();
    private final Connection natsConnection;
    private String natsHost = "localhost:4222";

    NatsClient() {
        this.serverURI = "jnats://".concat(this.natsHost);
        natsConnection = initConnection(serverURI);
    }

    public NatsClient(String serverURI) {
        if ((serverURI != null) && (!serverURI.isEmpty())) {
            this.serverURI = serverURI;
        } else {
            this.serverURI = "jnats://".concat(this.natsHost);
        }

        natsConnection = initConnection(serverURI);
    }

    public Map<String, Subscription> getSubscriptions() {
        return subscriptions;
    }

    private Connection initConnection(String uri) {
        log.info("Initializing connection to " + uri);
        try {
            Options options = new Options.Builder()
                    .errorCb(ex -> log.error("Connection Exception: ", ex))
                    .disconnectedCb(event -> log.error("Channel disconnected: {}", event.getConnection()))
                    .reconnectedCb(event -> log.error("Reconnected to server: {}", event.getConnection()))
                    .build();
            Connection conn = Nats.connect(uri, options);
            log.info("Returning NATS Connection for " + conn);
            return conn;
        } catch (IOException ioe) {
            log.error("Error connecting to NATs! ", ioe);
            return null;
        }
    }

    public void publish(String topic, String replyTo, String message) {
        log.info("publish <--- " + topic + " " + message);
        try {
            natsConnection.publish(topic, replyTo, message.getBytes());
            log.info("Message published");
        } catch (IOException ioe) {
            log.error("Error publishing message: {} to {} ", message, topic, ioe);
        }
    }

    public Connection getNatsConnection() {
        return natsConnection;
    }
}
