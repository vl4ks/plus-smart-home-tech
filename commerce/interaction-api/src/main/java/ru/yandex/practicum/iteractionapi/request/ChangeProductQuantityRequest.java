package ru.yandex.practicum.iteractionapi.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ChangeProductQuantityRequest {
    @NotNull
    UUID productId;
    @NotNull
    @Min(0)
    Long newQuantity;
}
