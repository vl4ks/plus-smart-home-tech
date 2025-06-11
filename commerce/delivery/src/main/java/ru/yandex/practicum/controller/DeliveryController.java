package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.iteractionapi.dto.DeliveryDto;
import ru.yandex.practicum.iteractionapi.dto.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PutMapping
    public DeliveryDto planDelivery(@RequestBody @Valid DeliveryDto deliveryDto) {
        log.info("Создать новую доставку в БД {}", deliveryDto);
        return deliveryService.planDelivery(deliveryDto);
    }

    @PostMapping("/successful")
    public void deliverySuccessful(UUID deliveryId) {
        log.info("Эмуляция успешной доставки товара {}", deliveryId);
        deliveryService.deliverySuccessful(deliveryId);
    }

    @PostMapping("/picked")
    public void deliveryPicked(UUID deliveryId) {
        log.info("Эмуляция получения товара в доставку {}", deliveryId);
        deliveryService.deliveryPicked(deliveryId);
    }

    @PostMapping("/failed")
    public void deliveryFailed(UUID deliveryId) {
        log.info("Эмуляция неудачного вручения товара {}", deliveryId);
        deliveryService.deliveryFailed(deliveryId);
    }

    @PostMapping("/cost")
    public Double deliveryCost(OrderDto orderDto) {
        log.info("Расчёт полной стоимости доставки заказа {}", orderDto);
        return deliveryService.deliveryCost(orderDto);
    }
}
