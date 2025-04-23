package ru.yandex.practicum.collector.builders.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.collector.schemas.hub.BaseHubEvent;
import ru.yandex.practicum.collector.schemas.hub.HubEventType;
import ru.yandex.practicum.collector.schemas.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedBuilder extends BaseHubBuilder<ScenarioRemovedEventAvro> {
    public ScenarioRemovedBuilder(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    public ScenarioRemovedEventAvro toAvro(BaseHubEvent hubEvent) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(((ScenarioRemovedEvent) hubEvent).getName())
                .build();
    }

    @Override
    public HubEventType getEventType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
