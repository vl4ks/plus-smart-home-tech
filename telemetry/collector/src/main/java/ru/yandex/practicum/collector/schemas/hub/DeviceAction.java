package ru.yandex.practicum.collector.schemas.hub;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DeviceAction {
    @NotNull
    private String sensorId;

    @NotNull
    private DeviceActionType type;

    private int value;
}
