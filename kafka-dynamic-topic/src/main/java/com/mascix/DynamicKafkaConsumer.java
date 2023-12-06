package com.mascix;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Slf4j
public class DynamicKafkaConsumer {

    private Consumer<String, String> kafkaConsumer;

    String bootstrapServers;

    String groupId;

    String kafkaTopic;  // Topic is not injected, so it can be changed dynamically

    public void initializeConsumer(String bootstrapServers, String groupId, String kafkaTopic) {
        this.bootstrapServers = bootstrapServers;
        this.groupId = groupId;
        this.kafkaTopic = kafkaTopic;

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "DYNAMIC-CLIENT");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        kafkaConsumer = new KafkaConsumer<>(properties);
        subscribeToTopic();  // Initial subscription
    }

    private void subscribeToTopic() {
        kafkaConsumer.subscribe(Collections.singletonList(kafkaTopic));
    }

    public void updateTopic(String newTopic) {
        kafkaTopic = newTopic;
        kafkaConsumer.unsubscribe();
        kafkaConsumer.subscribe(Collections.singletonList(kafkaTopic));
    }

    public void consume() {
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(1));
        records.forEach(var -> {
            // Process the records as needed
            log.info("{} message: {}", var.topic(), var.value());
        });
    }

    public void closeConsumer() {
        kafkaConsumer.close();
    }
}
