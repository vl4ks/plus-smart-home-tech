package ru.yandex.practicum.iteractionapi.feignClient;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.iteractionapi.dto.PageableDto;
import ru.yandex.practicum.iteractionapi.dto.ProductDto;
import ru.yandex.practicum.iteractionapi.enums.ProductCategory;
import ru.yandex.practicum.iteractionapi.enums.QuantityState;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {
    @GetMapping("/{productId}")
    ProductDto findProductById(@PathVariable @NotNull UUID productId);

    @GetMapping
    List<ProductDto> findProductsByCategory(@RequestParam ProductCategory productCategory, @Valid PageableDto pageableDto);

    @PutMapping
    ProductDto createProduct(@RequestBody @Valid ProductDto productDto);

    @PostMapping
    ProductDto updateProduct(@RequestBody @Valid ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    Boolean deleteProduct(@RequestBody @NotNull UUID productId);

    @PostMapping("/quantityState")
    Boolean setProductQuantityState(@RequestParam UUID productId, @RequestParam QuantityState quantityState);
}
