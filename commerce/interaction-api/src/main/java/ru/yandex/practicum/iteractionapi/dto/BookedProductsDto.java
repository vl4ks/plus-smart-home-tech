package ru.yandex.practicum.iteractionapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookedProductsDto {
    @NotNull
    Double deliveryWeight;
    @NotNull
    Double deliveryVolume;
    @NotNull
    Boolean fragile;
}
