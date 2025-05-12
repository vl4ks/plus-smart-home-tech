package ru.yandex.practicum.iteractionapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ShoppingCartDto {
    @NotNull
    UUID shoppingCartId;
    @NotNull
    Map<UUID, Long> products;
}
