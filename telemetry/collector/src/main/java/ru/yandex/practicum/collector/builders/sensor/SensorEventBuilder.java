package ru.yandex.practicum.collector.builders.sensor;

import ru.yandex.practicum.collector.schemas.sensor.BaseSensorEvent;
import ru.yandex.practicum.collector.schemas.sensor.SensorEventType;

public interface SensorEventBuilder {
    SensorEventType getEventType();

    void builder(BaseSensorEvent event);
}
