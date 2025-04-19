package ru.yandex.practicum.collector.schemas.sensor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "Событие датчика температуры.")
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
