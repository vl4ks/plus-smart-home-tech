package ru.yandex.practicum.shoppingstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.iteractionapi.enums.ProductCategory;
import ru.yandex.practicum.shoppingstore.model.Product;

import java.util.UUID;

public interface ShoppingStoreRepository extends JpaRepository<Product, UUID> {
    Page<Product> findAllByProductCategory(ProductCategory productCategory, Pageable pageable);
}
