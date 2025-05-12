package ru.yandex.practicum.shoppingcart.service;

import ru.yandex.practicum.iteractionapi.dto.BookedProductsDto;
import ru.yandex.practicum.iteractionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.iteractionapi.request.ChangeProductQuantityRequest;

import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto findShoppingCartByUser(String username);

    ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Long> request);

    void deactivateShoppingCartByUser(String username);

    ShoppingCartDto deleteProductsFromShoppingCart(String username, Map<UUID, Long> request);

    ShoppingCartDto updateProductQuantity(String username, ChangeProductQuantityRequest requestDto);

    BookedProductsDto bookingCartProducts(String username);
}
