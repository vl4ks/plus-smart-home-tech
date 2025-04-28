package ru.yandex.practicum.collector.builders.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorEventBuilder {
    SensorEventProto.PayloadCase getEventType();

    void builder(SensorEventProto event);
}
