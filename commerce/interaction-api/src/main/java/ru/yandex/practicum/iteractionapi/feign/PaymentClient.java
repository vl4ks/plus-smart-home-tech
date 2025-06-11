package ru.yandex.practicum.iteractionapi.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.iteractionapi.dto.OrderDto;
import ru.yandex.practicum.iteractionapi.dto.PaymentDto;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient {

    @PostMapping
    PaymentDto payment(@RequestBody @Valid OrderDto orderDto);

    @PostMapping("/totalCost")
    Double getTotalCost(@RequestBody @Valid OrderDto orderDto);

    @PostMapping("/productCost")
    Double productCost(@RequestBody @Valid OrderDto orderDto);

}
