package ru.yandex.practicum.collector.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CollectorKafkaProducer implements AutoCloseable {
    private final KafkaProducer<String, SpecificRecordBase> producer;

    private static final long SEND_TIMEOUT_MS = 5000;

    public void send(ProducerRecord<String, SpecificRecordBase> record) {
        if (record == null) {
            log.warn("Попытка отправить null-запись в Kafka");
            return;
        }

        try {
            log.debug("Отправка записи в топик {}: key={}, value={}",
                    record.topic(), record.key(), record.value());

            RecordMetadata metadata = producer.send(record)
                    .get(SEND_TIMEOUT_MS, TimeUnit.MILLISECONDS);

            log.info("Сообщение успешно отправлено в топик {}, partition {}, offset {}",
                    metadata.topic(), metadata.partition(), metadata.offset());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Отправка сообщения прервана: топик {}", record.topic(), e);
        } catch (ExecutionException e) {
            log.error("Ошибка при отправке сообщения в топик {}", record.topic(), e);
        } catch (TimeoutException e) {
            log.error("Таймаут при отправке сообщения в топик {}", record.topic(), e);
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
