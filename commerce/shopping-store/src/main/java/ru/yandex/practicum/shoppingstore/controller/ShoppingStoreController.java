package ru.yandex.practicum.shoppingstore.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.iteractionapi.dto.PageableDto;
import ru.yandex.practicum.iteractionapi.dto.ProductDto;
import ru.yandex.practicum.iteractionapi.enums.ProductCategory;
import ru.yandex.practicum.iteractionapi.request.SetProductQuantityStateRequest;
import ru.yandex.practicum.shoppingstore.service.ShoppingStoreService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController {
    private final ShoppingStoreService shoppingStoreService;

    @GetMapping("/{productId}")
    public ProductDto findProductById(@PathVariable @NotNull UUID productId) {
        log.info("Получение сведений по товару из БД: {}", productId);
        return shoppingStoreService.findProductById(productId);
    }

    @GetMapping
    public List<ProductDto> findProductsByCategory(@RequestParam ProductCategory productCategory,
                                                   @Valid PageableDto pageableDto) {
        log.info("Получение списка товаров по типу в пагинированном виде.");
        return shoppingStoreService.findProductsByCategory(productCategory, pageableDto);
    }

    @PutMapping
    public ProductDto createProduct(@RequestBody @Valid ProductDto productDto) {
        log.info("Создание нового товара в ассортименте {}", productDto);
        return shoppingStoreService.createProduct(productDto);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody @Valid ProductDto productDto) {
        log.info("Обновление товара в ассортименте {}", productDto);
        return shoppingStoreService.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public Boolean deleteProduct(@RequestBody @NotNull UUID productId) {
        log.info("Удаление товара из ассортимента магазина. Функция для менеджерского состава. {}", productId);
        return shoppingStoreService.deleteProduct(productId);
    }

    @PostMapping("/quantityState")
    public Boolean setProductQuantityState(@Valid SetProductQuantityStateRequest setProductQuantityStateRequest) {
        log.info("Установка статуса по товару {}", setProductQuantityStateRequest);
        return shoppingStoreService.setProductQuantityState(setProductQuantityStateRequest);
    }
}
