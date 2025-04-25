package ru.yandex.practicum.collector.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.collector.builders.hub.HubEventBuilder;
import ru.yandex.practicum.collector.builders.sensor.SensorEventBuilder;
import ru.yandex.practicum.collector.schemas.hub.BaseHubEvent;
import ru.yandex.practicum.collector.schemas.hub.HubEventType;
import ru.yandex.practicum.collector.schemas.sensor.BaseSensorEvent;
import ru.yandex.practicum.collector.schemas.sensor.SensorEventType;

import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class CollectorController {
    private final Map<SensorEventType, SensorEventBuilder> sensorBuilders;
    private final Map<HubEventType, HubEventBuilder> hubBuilders;

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody BaseSensorEvent sensor) {
        if (sensorBuilders.containsKey(sensor.getType())) {
            log.info("Обработка события датчика: {}", sensor);
            sensorBuilders.get(sensor.getType()).builder(sensor);
        } else {
            log.warn("Неизвестный тип события датчика: {}", sensor.getType());
        }
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody BaseHubEvent hub) {
        if (hubBuilders.containsKey(hub.getType())) {
            log.info("Обработка события хаба: {}", hub);
            hubBuilders.get(hub.getType()).builder(hub);
        } else {
            log.warn("Неизвестный тип события хаба: {}", hub.getType());
        }
    }
}
