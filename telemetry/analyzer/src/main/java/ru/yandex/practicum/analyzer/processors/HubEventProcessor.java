package ru.yandex.practicum.analyzer.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.handlers.HubEventHandler;
import ru.yandex.practicum.analyzer.handlers.HubHandler;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventProcessor implements Runnable {
    private final Consumer<String, HubEventAvro> consumer;
    private final HubHandler hubHandler;

    @Value("${topic.hub-event-topic}")
    private String topic;

    @Override
    public void run() {
        try {
            log.info("Запуск обработчика событий для топика: {}", topic);
            consumer.subscribe(List.of(topic));
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("Получен сигнал завершения работы, поднимаем потребителя...");
                consumer.wakeup();
            }));

            Map<String, HubEventHandler> eventHandlers = hubHandler.getHandlers();
            log.debug("Доступно обработчиков событий: {}", eventHandlers.size());

            while (true) {
                log.trace("Ожидание новых событий...");
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(Duration.ofMillis(1000));

                if (!records.isEmpty()) {
                    log.info("Получено {} событий для обработки", records.count());
                }

                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    HubEventAvro event = record.value();
                    String eventType = event.getPayload().getClass().getSimpleName();
                    String hubId = event.getHubId();

                    log.info("Обработка события {} для хаба {}, смещение: {}",
                            eventType, hubId, record.offset());

                    HubEventHandler handler = eventHandlers.get(eventType);
                    if (handler != null) {
                        log.debug("Найден обработчик для типа события {}", eventType);
                        handler.handle(event);
                        log.info("Событие {} для хаба {} успешно обработано",
                                eventType, hubId);
                    } else {
                        throw new IllegalArgumentException("Не найден обработчик для типа события " + eventType);
                    }
                }
                log.debug("Фиксация смещений для обработанных событий");
                consumer.commitSync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка в цикле обработки событий по топику {}", topic);
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }
}
