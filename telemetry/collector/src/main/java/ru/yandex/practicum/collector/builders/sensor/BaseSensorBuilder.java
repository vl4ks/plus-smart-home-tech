package ru.yandex.practicum.collector.builders.sensor;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.collector.schemas.sensor.BaseSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@RequiredArgsConstructor
public abstract class BaseSensorBuilder<T extends SpecificRecordBase> implements SensorEventBuilder {
    private final CollectorKafkaProducer producer;

    @Value("${kafka.topics.sensor}")
    private String topic;

    protected abstract T toAvro(BaseSensorEvent sensorEvent);

    @Override
    public void builder(BaseSensorEvent event) {
        T payload = toAvro(event);

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();

        producer.send(
                topic,
                event.getTimestamp(),
                event.getId(),
                sensorEventAvro
        );
    }
}
