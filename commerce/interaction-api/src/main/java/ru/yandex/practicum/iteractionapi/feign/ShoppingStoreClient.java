package ru.yandex.practicum.iteractionapi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.iteractionapi.enums.QuantityState;

import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {
    @PostMapping("/quantityState")
    Boolean setProductQuantityState(@RequestParam UUID productId, @RequestParam QuantityState quantityState);
}
