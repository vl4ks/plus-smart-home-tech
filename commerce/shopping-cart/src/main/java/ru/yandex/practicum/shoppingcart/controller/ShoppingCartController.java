package ru.yandex.practicum.shoppingcart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.iteractionapi.dto.BookedProductsDto;
import ru.yandex.practicum.iteractionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.iteractionapi.request.ChangeProductQuantityRequest;
import ru.yandex.practicum.shoppingcart.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartDto findCart(@RequestParam String username) {
        log.info("Получение актуальной корзины для авторизованного пользователя. {}", username);
        return shoppingCartService.findCart(username);
    }

    @PutMapping
    public ShoppingCartDto addProductToShoppingCart(@RequestParam String username,
                                                    @RequestBody Map<UUID, Long> request) {
        log.info("Добавление товара в корзину {}", username);
        return shoppingCartService.addProductToShoppingCart(username, request);
    }

    @DeleteMapping
    public void deactivateShoppingCartByUser(@RequestParam String username) {
        log.info("Деактивация корзины товаров для пользователя {}", username);
        shoppingCartService.deactivateShoppingCartByUser(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto deleteProductsFromShoppingCart(@RequestParam String username,
                                                          @RequestBody List<UUID> products) {
        log.info("Изменение состава товаров в корзине {}", username);
        return shoppingCartService.deleteProductsFromShoppingCart(username, products);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto updateProductQuantity(@RequestParam String username,
                                                 @RequestBody @Valid ChangeProductQuantityRequest requestDto) {
        log.info("Изменение количества товаров в корзине. {}", username);
        return shoppingCartService.updateProductQuantity(username, requestDto);
    }
}
