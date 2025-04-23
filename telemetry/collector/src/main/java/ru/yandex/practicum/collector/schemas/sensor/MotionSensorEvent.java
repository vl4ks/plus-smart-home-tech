package ru.yandex.practicum.collector.schemas.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MotionSensorEvent extends BaseSensorEvent {
    @NotNull
    private Integer linkQuality;
    @NotNull
    private Boolean motion;
    @NotNull
    private Integer voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
