package ru.yandex.practicum.collector.builders.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.collector.schemas.hub.BaseHubEvent;
import ru.yandex.practicum.collector.schemas.hub.DeviceAddedEvent;
import ru.yandex.practicum.collector.schemas.hub.HubEventType;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

@Component
public class DeviceAddedBuilder extends BaseHubBuilder<DeviceAddedEventAvro> {
    public DeviceAddedBuilder(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    public DeviceAddedEventAvro toAvro(BaseHubEvent hubEvent) {
        DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) hubEvent;
        DeviceTypeAvro deviceTypeAvro = DeviceTypeAvro.valueOf(deviceAddedEvent.getDeviceType().name());

        return DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEvent.getId())
                .setType(deviceTypeAvro)
                .build();
    }

    @Override
    public HubEventType getEventType() {
        return HubEventType.DEVICE_ADDED;
    }
}
