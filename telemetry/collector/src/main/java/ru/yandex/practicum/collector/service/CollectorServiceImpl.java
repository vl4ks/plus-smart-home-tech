package ru.yandex.practicum.collector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.collector.builders.hub.HubEventBuilder;
import ru.yandex.practicum.collector.builders.sensor.SensorEventBuilder;
import ru.yandex.practicum.collector.schemas.hub.BaseHubEvent;
import ru.yandex.practicum.collector.schemas.hub.HubEventType;
import ru.yandex.practicum.collector.schemas.sensor.BaseSensorEvent;
import ru.yandex.practicum.collector.schemas.sensor.SensorEventType;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollectorServiceImpl implements CollectorService {
    private final Map<SensorEventType, SensorEventBuilder> sensorEventBuilders;
    private final Map<HubEventType, HubEventBuilder> hubEventBuilders;

    @Override
    public void collectSensorEvent(BaseSensorEvent sensor) {
        try {
            log.info("Обработка события датчика: type={}, deviceId={}, timestamp={}",
                    sensor.getType(), sensor.getId(), sensor.getTimestamp());

            SensorEventBuilder builder = sensorEventBuilders.get(sensor.getType());
            if (builder == null) {
                log.error("Не найден конструктор для типа датчика: {}", sensor.getType());
                throw new IllegalArgumentException("Неподдерживаемый тип датчика: " + sensor.getType());
            }

            builder.builder(sensor);
            log.debug("Успешно обработанное событие датчика: deviceId={}", sensor.getId());

        } catch (Exception e) {
            log.error("Не удалось обработать событие датчика: deviceId={}, error={}",
                    sensor != null ? sensor.getId() : "null",
                    e.getMessage(),
                    e);
            throw e;
        }
    }

    @Override
    public void collectHubEvent(BaseHubEvent hub) {
        try {
            log.info("Обработка события хаба: type={}, hubId={}, timestamp={}",
                    hub.getType(), hub.getHubId(), hub.getTimestamp());

            HubEventBuilder builder = hubEventBuilders.get(hub.getType());
            if (builder == null) {
                log.error("Не найден конструктор для типа хаба: {}", hub.getType());
                throw new IllegalArgumentException("Неподдерживаемый тип хаба: " + hub.getType());
            }

            builder.builder(hub);
            log.debug("Успешно обработанное событие хаба: hubId={}", hub.getHubId());

        } catch (Exception e) {
            log.error("Не удалось обработать событие хаба: hubId={}, error={}",
                    hub != null ? hub.getHubId() : "null",
                    e.getMessage(),
                    e);
            throw e;
        }
    }
}
