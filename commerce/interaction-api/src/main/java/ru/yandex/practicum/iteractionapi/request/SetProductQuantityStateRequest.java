package ru.yandex.practicum.iteractionapi.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.iteractionapi.enums.QuantityState;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SetProductQuantityStateRequest {
    @NotNull
    UUID productId;
    @NotNull
    QuantityState quantityState;
}
