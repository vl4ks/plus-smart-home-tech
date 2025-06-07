package ru.yandex.practicum.iteractionapi.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.iteractionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.iteractionapi.request.ChangeProductQuantityRequest;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartClient {
    @GetMapping
    ShoppingCartDto findShoppingCartByUser(@RequestParam String username);

    @PutMapping
    ShoppingCartDto addProductToShoppingCart(@RequestParam String username,
                                             @RequestBody Map<UUID, Long> request);

    @DeleteMapping
    void deactivateShoppingCartByUser(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto deleteProductsFromShoppingCart(@RequestParam String username,
                                                   @RequestBody Map<UUID, Long> request);

    @PostMapping("/change-quantity")
    ShoppingCartDto updateProductQuantity(@RequestParam String username, @RequestBody @Valid ChangeProductQuantityRequest requestDto);

}
