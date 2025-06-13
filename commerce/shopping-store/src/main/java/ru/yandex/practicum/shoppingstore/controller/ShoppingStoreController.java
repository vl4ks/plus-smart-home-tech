package ru.yandex.practicum.shoppingstore.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.iteractionapi.dto.ProductDto;
import ru.yandex.practicum.iteractionapi.enums.QuantityState;
import ru.yandex.practicum.iteractionapi.request.SetProductQuantityStateRequest;
import ru.yandex.practicum.shoppingstore.service.ShoppingStoreService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController {
    private final ShoppingStoreService shoppingStoreService;

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable @NotNull UUID productId) {
        log.info("Получение сведений по товару из БД: {}", productId);
        return shoppingStoreService.getProduct(productId);
    }

    @GetMapping
    public Page<ProductDto> findProductsByCategory(@RequestParam String category,
                                                   @RequestParam(defaultValue = "0") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer size,
                                                   @RequestParam(defaultValue = "productName") String sort) {
        log.info("Получение списка товаров по типу в пагинированном виде.");
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        return shoppingStoreService.findProductsByProductCategory(category, pageable);
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
    public Boolean setProductQuantityState(UUID productId, QuantityState quantityState) {
        SetProductQuantityStateRequest request = SetProductQuantityStateRequest.builder()
                .productId(productId)
                .quantityState(quantityState)
                .build();

        log.info("Установка статуса товару {}", request);
        return shoppingStoreService.setProductQuantityState(request);
    }
}
