package ru.yandex.practicum.collector.builders.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.collector.schemas.sensor.BaseSensorEvent;
import ru.yandex.practicum.collector.schemas.sensor.ClimateSensorEvent;
import ru.yandex.practicum.collector.schemas.sensor.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorBuilder extends BaseSensorBuilder<ClimateSensorAvro> {
    public ClimateSensorBuilder(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    protected ClimateSensorAvro toAvro(BaseSensorEvent sensorEvent) {
        ClimateSensorEvent climateSensorEvent = (ClimateSensorEvent) sensorEvent;
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(climateSensorEvent.getTemperatureC())
                .setHumidity(climateSensorEvent.getHumidity())
                .setCo2Level(climateSensorEvent.getCo2Level())
                .build();
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

}
