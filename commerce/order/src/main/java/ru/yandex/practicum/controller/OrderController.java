package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.iteractionapi.dto.OrderDto;
import ru.yandex.practicum.iteractionapi.request.CreateNewOrderRequest;
import ru.yandex.practicum.iteractionapi.request.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getOrders(@RequestParam String username) {
        log.info("Получить заказы пользователя {}", username);
        return orderService.getOrders(username);
    }

    @PutMapping
    public OrderDto createNewOrder(@RequestBody @Valid CreateNewOrderRequest newOrderRequest) {
        log.info("Создать новый заказ в системе {}", newOrderRequest);
        return orderService.createNewOrder(newOrderRequest);
    }

    @PostMapping("/return")
    public OrderDto returnProduct(@RequestBody @Valid ProductReturnRequest returnRequest) {
        log.info("Возврат заказа {}", returnRequest);
        return orderService.returnProduct(returnRequest);
    }

    @PostMapping("/payment")
    public OrderDto payment(@RequestBody UUID orderId) {
        log.info("Оплата заказа {}", orderId);
        return orderService.payment(orderId);

    }

    @PostMapping("/payment/failed")
    public OrderDto paymentFailed(@RequestBody UUID orderId) {
        log.info("Оплата заказа произошла с ошибкой {}", orderId);
        return orderService.paymentFailed(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto delivery(@RequestBody UUID orderId) {
        log.info("Доставка заказа {}", orderId);
        return orderService.delivery(orderId);
    }

    @PostMapping("/delivery/failed")
    public OrderDto deliveryFailed(@RequestBody UUID orderId) {
        log.info("Доставка заказа произошла с ошибкой {}", orderId);
        return orderService.deliveryFailed(orderId);
    }

    @PostMapping("/completed")
    public OrderDto complete(@RequestBody UUID orderId) {
        log.info("Завершение заказа {}", orderId);
        return orderService.complete(orderId);
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateTotalCost(@RequestBody UUID orderId) {
        log.info("Расчёт стоимости заказа {}", orderId);
        return orderService.calculateTotalCost(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDeliveryCost(@RequestBody UUID orderId) {
        log.info("Расчёт стоимости доставки заказа {}", orderId);
        return orderService.calculateDeliveryCost(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto assembly(@RequestBody UUID orderId) {
        log.info("Сборка заказа {}", orderId);
        return orderService.assembly(orderId);
    }

    @PostMapping("/assembly/failed")
    public OrderDto assemblyFailed(@RequestBody UUID orderId) {
        log.info("Сборка заказа произошла с ошибкой {}", orderId);
        return orderService.assemblyFailed(orderId);
    }
}
