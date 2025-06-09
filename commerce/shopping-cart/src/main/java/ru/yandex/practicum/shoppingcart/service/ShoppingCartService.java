package ru.yandex.practicum.shoppingcart.service;

import ru.yandex.practicum.iteractionapi.dto.BookedProductsDto;
import ru.yandex.practicum.iteractionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.iteractionapi.request.ChangeProductQuantityRequest;
import ru.yandex.practicum.shoppingcart.model.ShoppingCart;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto findCart(String username);

    ShoppingCart findShoppingCartByUser(String username);

    ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Long> request);

    void deactivateShoppingCartByUser(String username);

    ShoppingCartDto deleteProductsFromShoppingCart(String username, List<UUID> products);

    ShoppingCartDto updateProductQuantity(String username, ChangeProductQuantityRequest requestDto);

    BookedProductsDto bookingProductsForUser(String username);
}
