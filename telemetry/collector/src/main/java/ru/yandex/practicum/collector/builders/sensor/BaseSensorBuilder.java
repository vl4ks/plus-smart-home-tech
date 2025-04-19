package ru.yandex.practicum.collector.builders.sensor;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.collector.producer.KafkaProducer;
import ru.yandex.practicum.collector.schemas.sensor.BaseSensorEvent;

@RequiredArgsConstructor
public abstract class BaseSensorBuilder implements SensorEventBuilder {
    private final KafkaProducer producer;

    @Value("${topic.telemetry-sensors}")
    private String topic;

    @Override
    public void builder(BaseSensorEvent event) {
        producer.send(toAvro(event), event.getHubId(), event.getTimestamp(), topic);
    }

    public abstract SpecificRecordBase toAvro(BaseSensorEvent sensorEvent);
}
