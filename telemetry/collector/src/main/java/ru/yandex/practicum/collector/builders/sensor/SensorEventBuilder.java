package ru.yandex.practicum.collector.builders.sensor;

import ru.yandex.practicum.collector.schemas.sensor.BaseSensorEvent;

public interface SensorEventBuilder {
    void builder(BaseSensorEvent event);
}
