package ru.yandex.practicum.collector.schemas.hub;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScenarioCondition {
    @NotNull
    private String sensorId;

    @NotNull
    private ScenarioConditionType type;

    @NotNull
    private ScenarioConditionOperationType operation;

    private int value;
}
