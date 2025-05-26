package ru.yandex.practicum.shoppingstore.service;

import ru.yandex.practicum.iteractionapi.dto.PageableDto;
import ru.yandex.practicum.iteractionapi.dto.ProductDto;
import ru.yandex.practicum.iteractionapi.enums.ProductCategory;
import ru.yandex.practicum.iteractionapi.request.SetProductQuantityStateRequest;

import java.util.List;
import java.util.UUID;

public interface ShoppingStoreService {
    ProductDto findProductById(UUID productId);

    List<ProductDto> findProductsByProductCategory(ProductCategory productCategory, PageableDto pageableDto);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean deleteProduct(UUID productId);

    boolean setProductQuantityState(SetProductQuantityStateRequest setProductQuantityStateRequest);

}
