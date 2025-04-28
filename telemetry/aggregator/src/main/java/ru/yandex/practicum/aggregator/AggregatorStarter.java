package ru.yandex.practicum.aggregator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregatorStarter {
    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);

    @Value("${kafka.topics.out}")
    private String outTopic;

    @Value("${kafka.topics.in}")
    private String inTopic;

    private final KafkaProducer<String, SensorsSnapshotAvro> producer;
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final SnapshotProcessor snapshotProcessor;

    public void start() {
        log.info("Запуск агрегатора. Подписываемся на топик: {}", inTopic);
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

        try {
            consumer.subscribe(List.of(inTopic));
            log.info("Успешно подписался на топик: {}", inTopic);
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    log.info("Обработка полученных данных {}", record.value());
                    snapshotProcessor.updateState(record.value())
                            .ifPresent(snapshotAvro -> {
                                log.info("Отправка снимка в топик {}: {}", outTopic, snapshotAvro);
                                producer.send(new ProducerRecord<>(outTopic, snapshotAvro));
                            });
                }
                consumer.commitSync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка при обработке событий, поступающих от датчиков", e);
        } finally {
            try {
                producer.flush();
                consumer.commitSync();

            } finally {
                log.info("Закрываем consumer");
                consumer.close();
                log.info("Закрываем producer");
                producer.close();
            }
        }
    }
}
