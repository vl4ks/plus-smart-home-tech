package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.iteractionapi.enums.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID orderId;
    String username;
    UUID shoppingCartId;
    @ElementCollection
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Long> products;
    UUID paymentId;
    UUID deliveryId;
    @Enumerated(EnumType.STRING)
    OrderState state;
    double deliveryWeight;
    double deliveryVolume;
    boolean fragile;
    @Column(precision = 19, scale = 4)
    BigDecimal totalPrice;

    @Column(precision = 19, scale = 4)
    BigDecimal deliveryPrice;

    @Column(precision = 19, scale = 4)
    BigDecimal productPrice;
}
