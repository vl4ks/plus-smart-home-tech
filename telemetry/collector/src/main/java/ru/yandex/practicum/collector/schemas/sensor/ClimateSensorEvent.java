package ru.yandex.practicum.collector.schemas.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ClimateSensorEvent extends BaseSensorEvent {
    @NotNull
    private int temperatureC;
    @NotNull
    private int humidity;
    @NotNull
    private int co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
