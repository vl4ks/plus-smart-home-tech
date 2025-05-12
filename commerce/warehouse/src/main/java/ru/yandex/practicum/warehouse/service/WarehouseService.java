package ru.yandex.practicum.warehouse.service;

import ru.yandex.practicum.iteractionapi.dto.AddressDto;
import ru.yandex.practicum.iteractionapi.dto.BookedProductsDto;
import ru.yandex.practicum.iteractionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.iteractionapi.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.iteractionapi.request.NewProductInWarehouseRequest;

public interface WarehouseService {
    void addNewProductToWarehouse(NewProductInWarehouseRequest newProductInWarehouseRequest);

    void addProductToWarehouse(AddProductToWarehouseRequest requestDto);

    BookedProductsDto checkProductQuantityForCart(ShoppingCartDto shoppingCartDto);

    AddressDto fetchWarehouseAddress();
}
