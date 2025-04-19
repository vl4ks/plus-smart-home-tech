package ru.yandex.practicum.collector.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.collector.schemas.hub.BaseHubEvent;
import ru.yandex.practicum.collector.schemas.sensor.BaseSensorEvent;
import ru.yandex.practicum.collector.service.CollectorService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class CollectorController {
    private final CollectorService collectorService;

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody BaseSensorEvent sensor) {
        try {
            log.info("Получено событие датчика: type={}, deviceId={}, timestamp={}",
                    sensor.getType(), sensor.getId(), sensor.getTimestamp());

            collectorService.collectSensorEvent(sensor);

            log.debug("Sensor событие успешно обработано: deviceId={}", sensor.getId());
        } catch (Exception e) {
            log.error("Не удалось обработать событие датчика: deviceId={}, error={}",
                    sensor != null ? sensor.getId() : "null", e.getMessage(), e);
            throw new RuntimeException("Не удалось обработать событие датчика", e);
        }
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody BaseHubEvent hub) {
        try {
            log.info("Полученное событие хаба: type={}, hubId={}, timestamp={}",
                    hub.getType(), hub.getHubId(), hub.getTimestamp());

            collectorService.collectHubEvent(hub);

            log.debug("Событие хаба успешно обработано: hubId={}", hub.getHubId());
        } catch (Exception e) {
            log.error("Не удалось обработать событие хаба: hubId={}, error={}",
                    hub != null ? hub.getHubId() : "null", e.getMessage(), e);
            throw new RuntimeException("Не удалось обработать событие хаба", e);
        }
    }
}
