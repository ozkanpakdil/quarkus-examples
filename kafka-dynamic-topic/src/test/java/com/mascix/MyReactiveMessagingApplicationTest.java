package com.mascix;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.kafka.KafkaClientService;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class MyReactiveMessagingApplicationTest {

    @Inject
    MyReactiveMessagingApplication application;

    @Inject
    @Channel("uppercase")
    Emitter<String> emitter;

    @Inject
    @Channel("channel1")
    Emitter<String> cemitter;

    @Inject
    @Channel("channel2")
    Emitter<String> c2emitter;

    @Inject
    @Channel("channel3")
    Emitter<String> c3emitter;

    @Inject
    KafkaClientService clientService;

    @Test
    void test() {
        Stream.of("uppercase").forEach(string -> emitter.send(string));
        assertEquals("BONJOUR", application.toUpperCase(Message.of("bonjour")).getPayload());
        Stream.of( "channel1").forEach(string -> cemitter.send(string));
        Stream.of("channel2").forEach(string -> c2emitter.send(string));

        Stream.of( "channel3","channel3","channel3","channel3","channel3").forEach(string -> c3emitter.send(string));
        String server = String.valueOf(clientService.getConsumer("channel3-out").configuration().get("bootstrap.servers"));
        application.sendSome(server);
    }
}
