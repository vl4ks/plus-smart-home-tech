package ru.yandex.practicum.iteractionapi.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.iteractionapi.dto.AddressDto;
import ru.yandex.practicum.iteractionapi.dto.ShoppingCartDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateNewOrderRequest {
    @NotNull
    ShoppingCartDto shoppingCart;
    @NotNull
    AddressDto deliveryAddress;
}
