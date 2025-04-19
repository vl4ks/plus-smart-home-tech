package ru.yandex.practicum.collector.builders.hub;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.collector.producer.KafkaProducer;
import ru.yandex.practicum.collector.schemas.hub.BaseHubEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseHubBuilder implements HubEventBuilder {
    private final KafkaProducer producer;

    @Value("${topic.telemetry-hubs}")
    private String topic;

    @Override
    public void builder(BaseHubEvent event) {
        producer.send(toAvro(event), event.getHubId(), event.getTimestamp(), topic);
    }

    public abstract SpecificRecordBase toAvro(BaseHubEvent hubEvent);
}
