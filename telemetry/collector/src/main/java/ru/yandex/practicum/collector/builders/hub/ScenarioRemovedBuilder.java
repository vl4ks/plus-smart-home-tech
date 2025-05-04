package ru.yandex.practicum.collector.builders.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedBuilder extends BaseHubBuilder<ScenarioRemovedEventAvro> {
    public ScenarioRemovedBuilder(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    public ScenarioRemovedEventAvro toAvro(HubEventProto hubEvent) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(hubEvent.getScenarioRemovedEvent().getName())
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getEventType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED_EVENT;
    }
}
