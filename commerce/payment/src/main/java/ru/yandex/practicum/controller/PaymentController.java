package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.iteractionapi.dto.OrderDto;
import ru.yandex.practicum.iteractionapi.dto.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public PaymentDto payment(@RequestBody @Valid OrderDto orderDto) {
        log.info("Формирование оплаты для заказа (переход в платежный шлюз): {}", orderDto);
        return paymentService.payment(orderDto);
    }

    @PostMapping("/totalCost")
    public BigDecimal getTotalCost(OrderDto orderDto) {
        log.info("Расчёт полной стоимости заказа: {}", orderDto);
        return paymentService.getTotalCost(orderDto);
    }

    @PostMapping("/refund")
    public void paymentSuccess(UUID orderId) {
        log.info("Метод для эмуляции успешной оплаты в платежного шлюза: {}", orderId);
        paymentService.paymentSuccess(orderId);
    }

    @PostMapping("/productCost")
    public BigDecimal productCost(OrderDto orderDto) {
        log.info("Расчёт стоимости товаров в заказе: {}", orderDto);
        return paymentService.productCost(orderDto);
    }

    @PostMapping("/failed")
    public void paymentFailed(UUID orderId) {
        log.info("Метод для эмуляции отказа в оплате платежного шлюза: {}", orderId);
        paymentService.paymentFailed(orderId);
    }
}
