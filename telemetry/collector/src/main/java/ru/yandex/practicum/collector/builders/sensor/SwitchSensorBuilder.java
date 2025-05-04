package ru.yandex.practicum.collector.builders.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorBuilder extends BaseSensorBuilder<SwitchSensorAvro> {
    public SwitchSensorBuilder(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    protected SwitchSensorAvro toAvro(SensorEventProto sensorEvent) {
        SwitchSensorProto switchSensorEvent = sensorEvent.getSwitchSensorEvent();
        return SwitchSensorAvro.newBuilder()
                .setState(switchSensorEvent.getState())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getEventType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }
}
