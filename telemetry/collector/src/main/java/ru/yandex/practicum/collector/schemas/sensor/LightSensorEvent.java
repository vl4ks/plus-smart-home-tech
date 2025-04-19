package ru.yandex.practicum.collector.schemas.sensor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "Событие датчика освещения.")
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
