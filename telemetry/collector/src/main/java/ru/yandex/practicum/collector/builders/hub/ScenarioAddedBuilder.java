package ru.yandex.practicum.collector.builders.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.CollectorKafkaProducer;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.*;


@Component
public class ScenarioAddedBuilder extends BaseHubBuilder<ScenarioAddedEventAvro> {
    public ScenarioAddedBuilder(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    public ScenarioAddedEventAvro toAvro(HubEventProto hubEvent) {
        ScenarioAddedEventProto event = hubEvent.getScenarioAddedEvent();

        return ScenarioAddedEventAvro.newBuilder()
                .setName(event.getName())
                .setActions(event.getActionList().stream()
                        .map(this::mapToDeviceActionAvro)
                        .toList())
                .setConditions(event.getConditionList().stream()
                        .map(this::mapToConditionTypeAvro)
                        .toList())
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getEventType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED_EVENT;
    }

    private ScenarioConditionAvro mapToConditionTypeAvro(ScenarioConditionProto condition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(condition.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().name()))
                .setValue(
                        switch (condition.getValueCase()) {
                            case INT_VALUE -> condition.getIntValue();
                            case BOOL_VALUE -> condition.getBoolValue() ? 1 : 0;
                            case VALUE_NOT_SET -> null;
                        }
                )
                .build();

    }

    private DeviceActionAvro mapToDeviceActionAvro(DeviceActionProto deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(ActionTypeAvro.valueOf(deviceAction.getType().name()))
                .setValue(deviceAction.getValue())
                .build();
    }
}

