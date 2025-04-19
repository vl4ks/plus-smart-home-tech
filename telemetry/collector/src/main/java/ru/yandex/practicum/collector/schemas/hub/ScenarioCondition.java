package ru.yandex.practicum.collector.schemas.hub;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class ScenarioCondition {
    private String sensorId;
    private ScenarioConditionType type;
    private ScenarioConditionOperationType operation;
    private int value;
}
