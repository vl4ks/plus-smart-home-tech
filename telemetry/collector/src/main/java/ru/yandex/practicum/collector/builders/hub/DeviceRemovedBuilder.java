package ru.yandex.practicum.collector.builders.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.collector.schemas.hub.BaseHubEvent;
import ru.yandex.practicum.collector.schemas.hub.DeviceRemovedEvent;
import ru.yandex.practicum.collector.schemas.hub.HubEventType;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Component
public class DeviceRemovedBuilder extends BaseHubBuilder<DeviceRemovedEventAvro> {
    public DeviceRemovedBuilder(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    public DeviceRemovedEventAvro toAvro(BaseHubEvent hubEvent) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(((DeviceRemovedEvent) hubEvent).getId())
                .build();
    }

    @Override
    public HubEventType getEventType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
