package ru.yandex.practicum.collector.schemas.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LightSensorEvent extends BaseSensorEvent {
    @NotNull
    private int linkQuality;
    @NotNull
    private int luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
