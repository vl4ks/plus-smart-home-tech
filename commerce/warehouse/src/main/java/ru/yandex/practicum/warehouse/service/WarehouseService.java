package ru.yandex.practicum.warehouse.service;

import ru.yandex.practicum.iteractionapi.dto.AddressDto;
import ru.yandex.practicum.iteractionapi.dto.BookedProductsDto;
import ru.yandex.practicum.iteractionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.iteractionapi.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.iteractionapi.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.iteractionapi.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.iteractionapi.request.ShippedToDeliveryRequest;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    void addNewProductToWarehouse(NewProductInWarehouseRequest newProductInWarehouseRequest);

    void addProductToWarehouse(AddProductToWarehouseRequest requestDto);

    BookedProductsDto checkProductQuantityForCart(ShoppingCartDto shoppingCartDto);

    AddressDto getWarehouseAddress();

    BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest assemblyProductsForOrder);

    void shippedToDelivery(ShippedToDeliveryRequest deliveryRequest);

    void acceptReturn(Map<UUID, Long> products);
}
