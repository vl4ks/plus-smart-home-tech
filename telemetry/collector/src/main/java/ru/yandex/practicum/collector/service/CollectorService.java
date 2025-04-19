package ru.yandex.practicum.collector.service;

import ru.yandex.practicum.collector.schemas.hub.BaseHubEvent;
import ru.yandex.practicum.collector.schemas.sensor.BaseSensorEvent;

public interface CollectorService {
    void collectSensorEvent(BaseSensorEvent sensor);

    void collectHubEvent(BaseHubEvent hub);
}
