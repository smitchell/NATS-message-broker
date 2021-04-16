package com.example.demo.services;

import io.nats.client.AsyncSubscription;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Options;
import io.nats.client.Subscription;
import io.nats.client.SyncSubscription;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public Map<String, Subscription> getSubscriptions() {
        return subscriptions;
    }

    private final Map<String, Subscription> subscriptions = new HashMap<>();
    private final Connection natsConnection;

    NatsClient() {
        this.serverURI = "jnats://localhost:4222";
        natsConnection = initConnection(serverURI);
    }

    public NatsClient(String serverURI) {
        if ((serverURI != null) && (!serverURI.isEmpty())) {
            this.serverURI = serverURI;
        } else {
            this.serverURI = "jnats://localhost:4222";
        }

        natsConnection = initConnection(serverURI);
    }

    private Connection initConnection(String uri) {
        try {
            Options options = new Options.Builder()
                    .errorCb(ex -> log.error("Connection Exception: ", ex))
                    .disconnectedCb(event -> log.error("Channel disconnected: {}", event.getConnection()))
                    .reconnectedCb(event -> log.error("Reconnected to server: {}", event.getConnection()))
                    .build();

            return Nats.connect(uri, options);
        } catch (IOException ioe) {
            log.error("Error connecting to NATs! ", ioe);
            return null;
        }
    }

    public void publish(String topic, String replyTo, String message) {
        try {
            natsConnection.publish(topic, replyTo, message.getBytes());
        } catch (IOException ioe) {
            log.error("Error publishing message: {} to {} ", message, topic, ioe);
        }
    }

    public Connection getNatsConnection() {
        return natsConnection;
    }
}
