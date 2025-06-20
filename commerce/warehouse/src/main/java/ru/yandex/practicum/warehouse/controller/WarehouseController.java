package ru.yandex.practicum.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.iteractionapi.dto.AddressDto;
import ru.yandex.practicum.iteractionapi.dto.BookedProductsDto;
import ru.yandex.practicum.iteractionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.iteractionapi.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.iteractionapi.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.iteractionapi.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.iteractionapi.request.ShippedToDeliveryRequest;
import ru.yandex.practicum.warehouse.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PutMapping
    public void addNewProductToWarehouse(@RequestBody @Valid NewProductInWarehouseRequest requestDto) {
        log.info("Добавление нового товара на склад {}", requestDto);
        warehouseService.addNewProductToWarehouse(requestDto);
    }

    @PostMapping("/add")
    public void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest requestDto) {
        log.info("Принятие товара на склад {}", requestDto);
        warehouseService.addProductToWarehouse(requestDto);
    }

    @PostMapping("/check")
    public BookedProductsDto checkProductQuantityForCart(@RequestBody @Valid ShoppingCartDto shoppingCartDto) {
        log.info("Проверка, что товаров достаточно для данной корзины {}", shoppingCartDto);
        return warehouseService.checkProductQuantityForCart(shoppingCartDto);
    }

    @GetMapping("/address")
    public AddressDto getWarehouseAddress() {
        log.info("Получение адреса склада для расчёта доставки.");
        return warehouseService.getWarehouseAddress();
    }

    @PostMapping("/shipped")
    public void shippedToDelivery(ShippedToDeliveryRequest deliveryRequest) {
        log.info("Передать товары в доставку {}", deliveryRequest);
        warehouseService.shippedToDelivery(deliveryRequest);
    }

    @PostMapping("/return")
    public void acceptReturn(@RequestBody Map<UUID, Long> products) {
        log.info("Принять возврат товаров на склад {}", products);
        warehouseService.acceptReturn(products);
    }

    @PostMapping("/assembly")
    public BookedProductsDto assemblyProductsForOrder(@RequestBody @Valid AssemblyProductsForOrderRequest assemblyProductsForOrder) {
        log.info("Собрать товары к заказу для подготовки к отправке {}",  assemblyProductsForOrder);
        return warehouseService.assemblyProductsForOrder(assemblyProductsForOrder);
    }
}
