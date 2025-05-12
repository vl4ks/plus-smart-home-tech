package ru.yandex.practicum.iteractionapi.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AddProductToWarehouseRequest {
    @NotNull
    UUID productId;
    @NotNull
    Long quantity;
}
