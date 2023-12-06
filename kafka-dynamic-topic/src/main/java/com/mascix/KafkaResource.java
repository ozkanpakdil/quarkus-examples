package com.mascix;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.Set;

@Path("/kafka")
@Slf4j
public class KafkaResource {

    @Inject
    MyReactiveMessagingApplication application;

    @GET
    public Set<String> list() {
        log.info("changing the topic");
        application.toUpperCase(Message.of("bonjour")).getPayload();
        DynamicKafkaConsumer consumer1 = new DynamicKafkaConsumer();
        consumer1.initializeConsumer("localhost:9092", "kafka-dynamic-topic", "dynamic-topic3");
        consumer1.consume();
        consumer1.closeConsumer();

        log.info("2changing the topic");

        DynamicKafkaConsumer consumer2 = new DynamicKafkaConsumer();
        consumer2.initializeConsumer("localhost:9092", "kafka-dynamic-topic", "dynamic-topic");
        consumer2.consume();
        consumer2.closeConsumer();

        return Set.of("kafka");
    }
}
