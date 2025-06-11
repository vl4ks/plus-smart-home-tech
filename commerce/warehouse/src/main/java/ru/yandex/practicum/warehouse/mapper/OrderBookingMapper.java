package ru.yandex.practicum.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.iteractionapi.dto.BookedProductsDto;
import ru.yandex.practicum.warehouse.model.OrderBooking;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderBookingMapper {
    BookedProductsDto toBookedProductsDto(OrderBooking booking);
}
