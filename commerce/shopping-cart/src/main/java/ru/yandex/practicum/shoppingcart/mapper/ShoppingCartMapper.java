package ru.yandex.practicum.shoppingcart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.iteractionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.shoppingcart.model.ShoppingCart;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {
    ShoppingCartMapper INSTANCE = Mappers.getMapper(ShoppingCartMapper.class);

    ShoppingCartDto toShoppingCartDto(ShoppingCart shoppingCart);
}
