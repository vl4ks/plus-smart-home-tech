package ru.yandex.practicum.iteractionapi.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.iteractionapi.circuitBreaker.WarehouseClientFallback;
import ru.yandex.practicum.iteractionapi.dto.AddressDto;
import ru.yandex.practicum.iteractionapi.dto.BookedProductsDto;
import ru.yandex.practicum.iteractionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.iteractionapi.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.iteractionapi.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.iteractionapi.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.iteractionapi.request.ShippedToDeliveryRequest;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse", fallback = WarehouseClientFallback.class)
public interface WarehouseClient {
    @PutMapping
    void addNewProductToWarehouse(@RequestBody @Valid NewProductInWarehouseRequest requestDto);

    @PostMapping("/add")
    void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest requestDto);

    @PostMapping("/check")
    BookedProductsDto checkProductQuantityForCart(@RequestBody @Valid ShoppingCartDto shoppingCartDto);

    @PostMapping("/return")
    void acceptReturn(@RequestBody Map<UUID, Long> products);

    @PostMapping("/assembly")
    BookedProductsDto assemblyProductForOrder(@RequestBody @Valid AssemblyProductsForOrderRequest assemblyProductsForOrder);

    @PostMapping("/shipped")
    void shippedToDelivery(ShippedToDeliveryRequest deliveryRequest);

    @GetMapping("/address")
    AddressDto getWarehouseAddress();
}
