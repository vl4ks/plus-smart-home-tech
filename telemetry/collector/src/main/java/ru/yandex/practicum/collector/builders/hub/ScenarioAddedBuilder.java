package ru.yandex.practicum.collector.builders.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.collector.schemas.hub.*;
import ru.yandex.practicum.kafka.telemetry.event.*;


@Component
public class ScenarioAddedBuilder extends BaseHubBuilder<ScenarioAddedEventAvro> {
    public ScenarioAddedBuilder(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    public ScenarioAddedEventAvro toAvro(BaseHubEvent hubEvent) {
        ScenarioAddedEvent event = (ScenarioAddedEvent) hubEvent;

        return ScenarioAddedEventAvro.newBuilder()
                .setName(event.getName())
                .setActions(event.getActions().stream()
                        .map(this::mapToDeviceActionAvro)
                        .toList())
                .setConditions(event.getConditions().stream()
                        .map(this::mapToConditionTypeAvro)
                        .toList())
                .build();
    }

    @Override
    public HubEventType getEventType() {
        return HubEventType.SCENARIO_ADDED;
    }

    private ScenarioConditionAvro mapToConditionTypeAvro(ScenarioCondition condition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(condition.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().name()))
                .setValue(condition.getValue())
                .build();

    }

    private DeviceActionAvro mapToDeviceActionAvro(DeviceAction deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(ActionTypeAvro.valueOf(deviceAction.getType().name()))
                .setValue(deviceAction.getValue())
                .build();
    }
}

