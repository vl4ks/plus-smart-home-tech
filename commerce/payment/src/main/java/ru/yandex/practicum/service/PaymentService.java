package ru.yandex.practicum.service;

import ru.yandex.practicum.iteractionapi.dto.OrderDto;
import ru.yandex.practicum.iteractionapi.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    PaymentDto payment(OrderDto orderDto);

    BigDecimal getTotalCost(OrderDto orderDto);

    void paymentSuccess(UUID uuid);

    BigDecimal productCost(OrderDto orderDto);

    void paymentFailed(UUID uuid);
}
