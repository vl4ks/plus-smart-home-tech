package ru.yandex.practicum.collector.builders.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.collector.schemas.sensor.BaseSensorEvent;
import ru.yandex.practicum.collector.schemas.sensor.LightSensorEvent;
import ru.yandex.practicum.collector.schemas.sensor.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorBuilder extends BaseSensorBuilder<LightSensorAvro> {
    public LightSensorBuilder(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    protected LightSensorAvro toAvro(BaseSensorEvent sensorEvent) {
        LightSensorEvent lightSensorEvent = (LightSensorEvent) sensorEvent;
        return LightSensorAvro.newBuilder()
                .setLinkQuality(lightSensorEvent.getLinkQuality())
                .setLuminosity(lightSensorEvent.getLuminosity())
                .build();
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
