package ru.yandex.practicum.collector.builders.sensor;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@RequiredArgsConstructor
public abstract class BaseSensorBuilder<T extends SpecificRecordBase> implements SensorEventBuilder {
    private final CollectorKafkaProducer producer;

    @Value("${kafka.topics.sensor}")
    private String topic;

    protected abstract T toAvro(SensorEventProto sensorEvent);

    @Override
    public void builder(SensorEventProto event) {
        T payload = toAvro(event);

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        event.getTimestamp().getSeconds(),
                        event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();

        producer.send(
                topic,
                Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()),
                event.getId(),
                sensorEventAvro
        );
    }
}
