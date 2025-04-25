package ru.yandex.practicum.collector.builders.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.collector.schemas.sensor.BaseSensorEvent;
import ru.yandex.practicum.collector.schemas.sensor.SensorEventType;
import ru.yandex.practicum.collector.schemas.sensor.SwitchSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorBuilder extends BaseSensorBuilder<SwitchSensorAvro> {
    public SwitchSensorBuilder(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    protected SwitchSensorAvro toAvro(BaseSensorEvent sensorEvent) {
        SwitchSensorEvent switchSensorEvent = (SwitchSensorEvent) sensorEvent;
        return SwitchSensorAvro.newBuilder()
                .setState(switchSensorEvent.getState())
                .build();
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
