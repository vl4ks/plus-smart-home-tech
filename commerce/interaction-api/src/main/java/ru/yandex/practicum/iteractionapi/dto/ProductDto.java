package ru.yandex.practicum.iteractionapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.iteractionapi.enums.ProductCategory;
import ru.yandex.practicum.iteractionapi.enums.ProductState;
import ru.yandex.practicum.iteractionapi.enums.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDto {
    UUID productId;
    @NotBlank
    String productName;
    @NotBlank
    String description;
    String imageSrc;
    @NotNull
    QuantityState quantityState;
    @NotNull
    ProductState productState;
    @Min(1)
    @Max(5)
    Integer rating;
    ProductCategory productCategory;
    @NotNull
    BigDecimal price;
}
