package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.iteractionapi.dto.OrderDto;
import ru.yandex.practicum.model.Order;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toOrderDto(Order order);

    List<OrderDto> toOrdersDto(List<Order> orders);
}
