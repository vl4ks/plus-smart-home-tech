package ru.yandex.practicum.aggregator;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
public class AggregatorKafkaConfig {
    private final String bootstrapServers;
    private final String groupId;
    private final String keyDeserializer;
    private final String valueDeserializer;
    private final String keySerializer;
    private final String valueSerializer;

    public AggregatorKafkaConfig(
            @Value("${kafka.bootstrap-servers}") String bootstrapServers,
            @Value("${kafka.consumer.group-id}") String groupId,
            @Value("${kafka.consumer.key-deserializer}") String keyDeserializer,
            @Value("${kafka.consumer.value-deserializer}") String valueDeserializer,
            @Value("${kafka.producer.key-serializer}") String keySerializer,
            @Value("${kafka.producer.value-serializer}") String valueSerializer) {
        this.bootstrapServers = bootstrapServers;
        this.groupId = groupId;
        this.keyDeserializer = keyDeserializer;
        this.valueDeserializer = valueDeserializer;
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
    }

    @Bean
    public KafkaProducer<String, SensorsSnapshotAvro> kafkaProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        return new KafkaProducer<>(config);
    }

    @Bean
    public KafkaConsumer<String, SensorEventAvro> kafkaConsumer() {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        return new KafkaConsumer<>(config);
    }
}
