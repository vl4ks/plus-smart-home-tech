package ru.yandex.practicum.collector.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CollectorKafkaProducer implements AutoCloseable {
    private final KafkaProducer<String, SpecificRecordBase> producer;

    public void send(ProducerRecord<String, SpecificRecordBase> record) {
        if (record != null) {
            producer.send(record);
        }
    }

    @Override
    public void close() throws Exception {
        producer.flush();
        producer.close();
    }
}
