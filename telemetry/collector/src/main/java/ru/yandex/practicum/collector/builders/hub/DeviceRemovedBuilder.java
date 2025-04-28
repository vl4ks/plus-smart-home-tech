package ru.yandex.practicum.collector.builders.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Component
public class DeviceRemovedBuilder extends BaseHubBuilder<DeviceRemovedEventAvro> {
    public DeviceRemovedBuilder(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    public DeviceRemovedEventAvro toAvro(HubEventProto hubEvent) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(hubEvent.getDeviceRemovedEvent().getId())
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getEventType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED_EVENT;
    }
}
