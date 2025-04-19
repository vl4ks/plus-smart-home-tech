package ru.yandex.practicum.collector.builders.hub;

import ru.yandex.practicum.collector.schemas.hub.BaseHubEvent;

public interface HubEventBuilder {
    void builder(BaseHubEvent hubEvent);
}
