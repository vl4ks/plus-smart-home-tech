package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.iteractionapi.enums.PaymentState;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID paymentId;
    UUID orderId;
    @Column(precision = 19, scale = 4)
    BigDecimal productsTotal;

    @Column(precision = 19, scale = 4)
    BigDecimal deliveryTotal;

    @Column(precision = 19, scale = 4)
    BigDecimal totalPayment;

    @Column(precision = 19, scale = 4)
    BigDecimal feeTotal;
    @Enumerated(EnumType.STRING)
    PaymentState status;
}
