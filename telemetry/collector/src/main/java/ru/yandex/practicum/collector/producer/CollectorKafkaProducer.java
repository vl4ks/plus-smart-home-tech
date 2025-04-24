package ru.yandex.practicum.collector.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@Component
@RequiredArgsConstructor
public class CollectorKafkaProducer implements AutoCloseable {
    private final KafkaProducer<String, SpecificRecordBase> producer;

    public void send(String topic, Instant timestamp, String hubId, SpecificRecordBase event) {
        if (event == null) {
            log.warn("Попытка отправить null-событие в Kafka");
            return;
        }

        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                topic,
                null,
                timestamp.toEpochMilli(),
                hubId,
                event
        );

        String eventClass = event.getClass().getSimpleName();

        Future<RecordMetadata> futureResult = producer.send(record);
        producer.flush();

        try {
            RecordMetadata metadata = futureResult.get();
            log.info("Событие {} было успешно сохранёно в топик {} в партицию {} со смещением {}",
                    eventClass, metadata.topic(), metadata.partition(), metadata.offset());
        } catch (InterruptedException | ExecutionException e) {
            log.warn("Не удалось записать событие {} в топик {}", eventClass, topic, e);
        }
    }

    @Override
    public void close() {
        try {
            log.info("Завершение работы Kafka producer...");
            producer.flush();
            producer.close();
            log.info("Kafka producer успешно закрыт");
        } catch (Exception e) {
            log.error("Ошибка при закрытии Kafka producer", e);
        }
    }
}
