package com.example.demo.services;

import com.example.demo.models.EventMessage;
import com.example.demo.models.LoadTestEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ProducerService implements CommandLineRunner {
    private final NatsClient natsClient;

    public ProducerService(final NatsClient natsClient) {
        this.natsClient = natsClient;
    }

    @Override
    public void run(String... args) throws Exception {
        final String routingKey = "test.message.load";
        final ObjectMapper mapper = new ObjectMapper();
        final EventMessage event = new EventMessage();
        event.setCreatedBy("smitchell");
        event.setEventType("LOAD_TEST");

        int i = 0;
        while (i <= 100) {
            event.setEventId(UUID.randomUUID().toString());
            LoadTestEvent loadTestEvent = new LoadTestEvent(i++, RandomStringUtils.random(25, true, false));
            event.setPayload(mapper.writeValueAsString(loadTestEvent));
            final String message = mapper.writeValueAsString(event);
            log.info("Publishing message " + message);
            natsClient.publish(NatsClient.TEST_TOPIC, null, message);
        }
    }
}
