package ru.yandex.practicum.iteractionapi.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.iteractionapi.dto.DimensionDto;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewProductInWarehouseRequest {
    @NotNull
    UUID productId;
    Boolean fragile;
    @NotNull
    DimensionDto dimension;
    @Min(1)
    Double weight;
}
