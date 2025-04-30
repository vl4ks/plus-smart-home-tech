package ru.yandex.practicum.aggregator.config;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class AggregatorKafkaProducerConfig {
    private final Environment environment;

    @Bean
    public KafkaProducer<String, SpecificRecordBase> getProducerProperties() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("kafka.bootstrap-servers"));
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, environment.getProperty("kafka.producer.key-serializer"));
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                environment.getProperty("kafka.producer.value-serializer"));

        return new KafkaProducer<>(properties);
    }
}
