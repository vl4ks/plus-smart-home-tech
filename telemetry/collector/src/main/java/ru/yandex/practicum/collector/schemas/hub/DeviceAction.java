package ru.yandex.practicum.collector.schemas.hub;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class DeviceAction {
    private String sensorId;
    private DeviceActionType type;
    private int value;
}
