package ru.yandex.practicum.iteractionapi.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.iteractionapi.enums.QuantityState;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetProductQuantityStateRequest {
    @NotNull
    UUID productId;
    @NotNull
    QuantityState quantityState;
}
