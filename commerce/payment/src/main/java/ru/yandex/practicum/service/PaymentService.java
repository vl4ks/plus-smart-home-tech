package ru.yandex.practicum.service;

import ru.yandex.practicum.iteractionapi.dto.OrderDto;
import ru.yandex.practicum.iteractionapi.dto.PaymentDto;

import java.util.UUID;

public interface PaymentService {
    PaymentDto createPayment(OrderDto orderDto);

    Double getTotalCost(OrderDto orderDto);

    void paymentSuccess(UUID uuid);

    Double productCost(OrderDto orderDto);

    void paymentFailed(UUID uuid);
}
