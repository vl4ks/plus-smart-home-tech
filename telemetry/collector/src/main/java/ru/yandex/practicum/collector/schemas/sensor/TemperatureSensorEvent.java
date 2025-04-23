package ru.yandex.practicum.collector.schemas.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TemperatureSensorEvent extends BaseSensorEvent {

    @NotNull
    private int temperatureC;
    @NotNull
    private int temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
