package com.example.demo.services;


import com.example.demo.models.EventMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.AsyncSubscription;
import io.nats.client.Connection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class ConsumerService {
    private final ObjectMapper mapper = new ObjectMapper();
    private final NatsClient natsClient;

    public ConsumerService(final NatsClient natsClient) {
        this.natsClient = natsClient;
        this.subscribeAsync();
    }

    private void subscribeAsync()  {
        final Connection conn = natsClient.getNatsConnection();
        AsyncSubscription subscription = conn.subscribe(
                NatsClient.TEST_TOPIC, msg -> {
                    try {
                        EventMessage event = mapper.readValue(msg.getData(), EventMessage.class);
                        System.out.println("Received event id " + event.getPayload());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        if (subscription == null) {
            log.error("Error subscribing to {}", NatsClient.TEST_TOPIC);
        } else {
            natsClient.getSubscriptions().put(NatsClient.TEST_TOPIC, subscription);
        }
    }
}
