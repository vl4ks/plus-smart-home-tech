package ru.yandex.practicum.iteractionapi.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.iteractionapi.dto.DimensionDto;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewProductInWarehouseRequest {
    @NotNull
    UUID productId;
    Boolean fragile;
    @NotNull
    DimensionDto dimension;
    @Min(1)
    Double weight;
}
