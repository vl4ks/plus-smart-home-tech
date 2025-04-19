package ru.yandex.practicum.collector.schemas.hub;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class DeviceRemovedEvent extends BaseHubEvent {
    @NotBlank
    private String id;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
