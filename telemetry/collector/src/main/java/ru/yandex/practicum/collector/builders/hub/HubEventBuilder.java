package ru.yandex.practicum.collector.builders.hub;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubEventBuilder {
    HubEventProto.PayloadCase getEventType();

    void builder(HubEventProto hubEvent);
}
