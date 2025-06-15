package ru.yandex.practicum.iteractionapi.circuitBreaker;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.iteractionapi.dto.AddressDto;
import ru.yandex.practicum.iteractionapi.dto.BookedProductsDto;
import ru.yandex.practicum.iteractionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.iteractionapi.feign.WarehouseClient;
import ru.yandex.practicum.iteractionapi.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.iteractionapi.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.iteractionapi.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.iteractionapi.request.ShippedToDeliveryRequest;

import java.util.Map;
import java.util.UUID;

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
    public AddressDto getWarehouseAddress() {
        throw new WarehouseServerUnavailable("Fallback response: сервис временно недоступен.");
    }

    @Override
    public void acceptReturn(Map<UUID, Long> products) {
        throw new WarehouseServerUnavailable("Fallback response: сервис временно недоступен.");
    }

    @Override
    public BookedProductsDto assemblyProductForOrder(AssemblyProductsForOrderRequest assemblyProductsForOrder) {
        throw new WarehouseServerUnavailable("Fallback response: сервис временно недоступен.");
    }

    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest deliveryRequest) {
        throw new WarehouseServerUnavailable("Fallback response: сервис временно недоступен.");
    }
}
