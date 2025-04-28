package ru.yandex.practicum.collector.builders.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

@Component
public class DeviceAddedBuilder extends BaseHubBuilder<DeviceAddedEventAvro> {
    public DeviceAddedBuilder(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    public DeviceAddedEventAvro toAvro(HubEventProto hubEvent) {
        DeviceAddedEventProto deviceAddedEvent = hubEvent.getDeviceAddedEvent();
        DeviceTypeAvro deviceTypeAvro = DeviceTypeAvro.valueOf(deviceAddedEvent.getType().name());

        return DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEvent.getId())
                .setType(deviceTypeAvro)
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getEventType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED_EVENT;
    }
}
