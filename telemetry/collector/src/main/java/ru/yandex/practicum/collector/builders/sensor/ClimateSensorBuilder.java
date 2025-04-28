package ru.yandex.practicum.collector.builders.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorBuilder extends BaseSensorBuilder<ClimateSensorAvro> {
    public ClimateSensorBuilder(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    protected ClimateSensorAvro toAvro(SensorEventProto sensorEvent) {
        ClimateSensorProto climateSensorEvent = sensorEvent.getClimateSensorEvent();
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(climateSensorEvent.getTemperatureC())
                .setHumidity(climateSensorEvent.getHumidity())
                .setCo2Level(climateSensorEvent.getCo2Level())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getEventType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }

}
