package ru.yandex.practicum.collector.schemas.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SwitchSensorEvent extends BaseSensorEvent {
    @NotNull
    private Boolean state;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
