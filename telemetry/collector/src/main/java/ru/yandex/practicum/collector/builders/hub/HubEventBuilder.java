package ru.yandex.practicum.collector.builders.hub;

import ru.yandex.practicum.collector.schemas.hub.BaseHubEvent;
import ru.yandex.practicum.collector.schemas.hub.HubEventType;

public interface HubEventBuilder {
    HubEventType getEventType();

    void builder(BaseHubEvent hubEvent);
}
