package com.mascix;

import io.smallrye.reactive.messaging.annotations.Merge;
import io.smallrye.reactive.messaging.kafka.KafkaClientService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.*;

import java.util.stream.Stream;

@ApplicationScoped
@Slf4j
public class MyReactiveMessagingApplication {

    public static final String CHANNEL_3 = "channel3";
    @Inject
    @Channel("words-out")
    Emitter<String> emitter;
    @Inject
    @Channel(CHANNEL_3)
    Emitter<String> c3emitter;

    @Inject
    KafkaClientService clientService;

    /**
     * Consume the message from the "words-in" channel, uppercase it and send it to the uppercase channel.
     * Messages come from the broker.
     **/
    @Incoming("words-in")
    @Outgoing("uppercase")
    public Message<String> toUpperCase(Message<String> message) {
        return message.withPayload(message.getPayload().toUpperCase());
    }

    /**
     * Consume the uppercase channel (in-memory) and print the messages.
     **/
    @Incoming("uppercase")
    @Merge
    public void sink(String word) {
        log.info(">>Incoming message:" + word);
    }

    @Incoming("channel1")
    public void consumer1(String word) {
        log.info("[channel1]>>Incoming message>> " + word);
    }

    @Incoming("channel2")
    public void consumer2(String word) {
        log.info("[channel2]>>Incoming message>> " + word);
    }

    public void sendSome(String bootstrapServers) {
        if (log.isDebugEnabled()) {
            log.info("producers:");
            clientService.getProducerChannels().forEach(log::info);
            log.info("consumers:");
            clientService.getConsumerChannels().forEach(log::info);
        }

        DynamicKafkaConsumer kafkaConsumer = new DynamicKafkaConsumer();
        kafkaConsumer.initializeConsumer(bootstrapServers.substring(10), "kafka-dynamic-topic", "dynamic-topic3");
        kafkaConsumer.consume();


        kafkaConsumer.updateTopic("dynamic-topic");
        kafkaConsumer.consume();
        Stream.of(CHANNEL_3, CHANNEL_3, CHANNEL_3, CHANNEL_3, CHANNEL_3).forEach(string -> c3emitter.send(string));
        kafkaConsumer.consume();
        kafkaConsumer.closeConsumer();
    }
}
