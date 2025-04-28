package ru.yandex.practicum.collector.builders.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorBuilder extends BaseSensorBuilder<LightSensorAvro> {
    public LightSensorBuilder(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    protected LightSensorAvro toAvro(SensorEventProto sensorEvent) {
        LightSensorProto lightSensorEvent = sensorEvent.getLightSensorEvent();
        return LightSensorAvro.newBuilder()
                .setLinkQuality(lightSensorEvent.getLinkQuality())
                .setLuminosity(lightSensorEvent.getLuminosity())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getEventType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }
}
