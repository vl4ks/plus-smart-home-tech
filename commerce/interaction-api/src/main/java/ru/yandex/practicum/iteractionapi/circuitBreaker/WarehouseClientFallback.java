package ru.yandex.practicum.iteractionapi.circuitBreaker;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.iteractionapi.dto.AddressDto;
import ru.yandex.practicum.iteractionapi.dto.BookedProductsDto;
import ru.yandex.practicum.iteractionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.iteractionapi.feign.WarehouseClient;
import ru.yandex.practicum.iteractionapi.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.iteractionapi.request.NewProductInWarehouseRequest;

@Component
public class WarehouseClientFallback implements WarehouseClient {

    @Override
    public void addNewProductToWarehouse(NewProductInWarehouseRequest requestDto) {
        throw new WarehouseServerUnavailable("Fallback response: сервис временно недоступен.");
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest requestDto) {
        throw new WarehouseServerUnavailable("Fallback response: сервис временно недоступен.");
    }

    @Override
    public BookedProductsDto checkProductQuantityForCart(ShoppingCartDto shoppingCartDto) {
        throw new WarehouseServerUnavailable("Fallback response: сервис временно недоступен.");
    }

    @Override
    public AddressDto fetchWarehouseAddress() {
        throw new WarehouseServerUnavailable("Fallback response: сервис временно недоступен.");
    }

    @Override
    public BookedProductsDto bookingCartProducts(ShoppingCartDto shoppingCartDto) {
        throw new WarehouseServerUnavailable("Fallback response: сервис временно недоступен.");
    }
}
