package ru.yandex.practicum.collector.builders.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionSensorBuilder extends BaseSensorBuilder<MotionSensorAvro> {
    public MotionSensorBuilder(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    protected MotionSensorAvro toAvro(SensorEventProto sensorEvent) {
        MotionSensorProto motionSensorEvent = sensorEvent.getMotionSensorEvent();
        return MotionSensorAvro.newBuilder()
                .setMotion(motionSensorEvent.getMotion())
                .setLinkQuality(motionSensorEvent.getLinkQuality())
                .setVoltage(motionSensorEvent.getVoltage())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getEventType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }
}
