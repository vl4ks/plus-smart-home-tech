package ru.yandex.practicum.collector.builders.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.collector.schemas.hub.BaseHubEvent;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@RequiredArgsConstructor
public abstract class BaseHubBuilder<T extends SpecificRecordBase> implements HubEventBuilder {
    private final CollectorKafkaProducer producer;

    @Value("${kafka.topics.hub}")
    private String topic;

    public abstract T toAvro(BaseHubEvent hubEvent);

    @Override
    public void builder(BaseHubEvent event) {
        T payload = toAvro(event);

        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();

        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, hubEventAvro);
        producer.send(record);
    }
}
