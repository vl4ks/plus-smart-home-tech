package ru.yandex.practicum.analyzer.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.analyzer.model.Sensor;
import ru.yandex.practicum.analyzer.repository.SensorRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceAdded implements HubEventHandler {
    private final SensorRepository sensorRepository;

    @Override
    @Transactional
//    public void handle(HubEventAvro hubEvent) {
//        Sensor savedSensor = sensorRepository.save(buildToSensor(hubEvent));
//        log.info("Датчик успешно сохранен в БД: id={}", savedSensor.getId());
//    }
    public void handle(HubEventAvro hubEvent) {
        DeviceAddedEventAvro deviceAddedEvent = (DeviceAddedEventAvro) hubEvent.getPayload();
        String deviceId = deviceAddedEvent.getId();
        String hubId = hubEvent.getHubId();

        sensorRepository.findById(deviceId)
                .ifPresentOrElse(
                        existingSensor -> {
                            if (!existingSensor.getHubId().equals(hubId)) {
                                existingSensor.setHubId(hubId);
                                sensorRepository.save(existingSensor);
                                log.info("Обновлен хаб для существующего датчика: id={}, новый hubId={}",
                                        deviceId, hubId);
                            } else {
                                log.debug("Датчик уже существует и привязан к этому хабу: id={}", deviceId);
                            }
                        },
                        () -> {
                            Sensor newSensor = sensorRepository.save(buildToSensor(hubEvent));
                            log.info("Датчик успешно сохранен в БД: id={}", newSensor.getId());
                        }
                );
    }

    @Override
    public String getMessageType() {
        return DeviceAddedEventAvro.class.getSimpleName();
    }

    private Sensor buildToSensor(HubEventAvro hubEvent) {
        DeviceAddedEventAvro deviceAddedEvent = (DeviceAddedEventAvro) hubEvent.getPayload();

        return Sensor.builder()
                .id(deviceAddedEvent.getId())
                .hubId(hubEvent.getHubId())
                .build();
    }
}
