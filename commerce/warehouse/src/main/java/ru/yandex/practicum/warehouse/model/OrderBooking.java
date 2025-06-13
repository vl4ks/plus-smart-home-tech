package ru.yandex.practicum.warehouse.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@Table(name = "order_booking")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderBooking {
    @Id
    UUID orderBookingId;
    UUID orderId;
    UUID deliveryId;
    Double deliveryWeight;
    Double deliveryVolume;
    Boolean fragile;
    @ElementCollection
    @CollectionTable(name = "booking_products", joinColumns =  @JoinColumn(name = "order_booking_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Long> products;
}
