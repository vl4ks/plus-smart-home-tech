package ru.yandex.practicum.shoppingstore.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.iteractionapi.enums.ProductCategory;
import ru.yandex.practicum.iteractionapi.enums.ProductState;
import ru.yandex.practicum.iteractionapi.enums.QuantityState;

import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID productId;
    String productName;
    String description;
    String imageSrc;
    @Enumerated(EnumType.STRING)
    QuantityState quantityState;
    @Enumerated(EnumType.STRING)
    ProductState productState;
    @Enumerated(EnumType.STRING)
    ProductCategory productCategory;
    double price;
    int rating;
}
