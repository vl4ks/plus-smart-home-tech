package ru.yandex.practicum.collector.builders.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@RequiredArgsConstructor
public abstract class BaseHubBuilder<T extends SpecificRecordBase> implements HubEventBuilder {
    private final CollectorKafkaProducer producer;

    @Value("${kafka.topics.hub}")
    private String topic;

    public abstract T toAvro(HubEventProto hubEvent);

    @Override
    public void builder(HubEventProto event) {
        T payload = toAvro(event);

        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();

        producer.send(
                topic,
                Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()),
                event.getHubId(),
                hubEventAvro
        );
    }
}
