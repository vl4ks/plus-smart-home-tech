package ru.yandex.practicum.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.iteractionapi.dto.AddressDto;
import ru.yandex.practicum.iteractionapi.dto.BookedProductsDto;
import ru.yandex.practicum.iteractionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.iteractionapi.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.iteractionapi.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.iteractionapi.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.iteractionapi.request.ShippedToDeliveryRequest;
import ru.yandex.practicum.warehouse.address.Address;
import ru.yandex.practicum.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.warehouse.exception.ProductNotFoundInWarehouseException;
import ru.yandex.practicum.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.warehouse.mapper.OrderBookingMapper;
import ru.yandex.practicum.warehouse.mapper.WarehouseMapper;
import ru.yandex.practicum.warehouse.model.OrderBooking;
import ru.yandex.practicum.warehouse.model.Warehouse;
import ru.yandex.practicum.warehouse.repository.OrderBookingRepository;
import ru.yandex.practicum.warehouse.repository.WarehouseRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.READ_COMMITTED)
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final OrderBookingRepository orderBookingRepository;
    private final OrderBookingMapper orderBookingMapper;

    @Override
    public void addNewProductToWarehouse(NewProductInWarehouseRequest request) {
        log.info("Добавление нового товара на склад. id товара: {}", request.getProductId());

        warehouseRepository.findById(request.getProductId()).ifPresent(warehouse -> {
            String errorMessage = "Товар с id =  " + request.getProductId() + " уже зарегистрирован на складе";
            log.error(errorMessage);
            throw new SpecifiedProductAlreadyInWarehouseException(errorMessage);
        });
        Warehouse warehouse = warehouseMapper.toWarehouse(request);
        if (warehouse.getQuantity() == null) {
            warehouse.setQuantity(0L);
        }
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        log.info("Товар успешно добавлен на склад. id: {}, Количество: {}",
                savedWarehouse.getProductId(),
                savedWarehouse.getQuantity());
    }


    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest request) {
        log.info("Добавление товара на склад. id товара: {}, Количество: {}",
                request.getProductId(),
                request.getQuantity());
        Warehouse product = warehouseRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Товар c id =" +
                        request.getProductId() + " не найден на складе."));

        product.setQuantity(product.getQuantity() + request.getQuantity());

        warehouseRepository.save(product);

    }

    public BookedProductsDto checkProductQuantityForCart(ShoppingCartDto shoppingCartDto) {
        log.info("Проверка наличия товаров для корзины. id корзины: {}", shoppingCartDto.getShoppingCartId());

        Map<UUID, Long> products = shoppingCartDto.getProducts();
        log.debug("Товары в корзине: {}", products);

        Set<UUID> cartProductIds = products.keySet();
        Map<UUID, Warehouse> warehouseProducts = warehouseRepository.findAllById(cartProductIds)
                .stream()
                .collect(Collectors.toMap(Warehouse::getProductId, Function.identity()));

        Set<UUID> productIds = warehouseProducts.keySet();

        cartProductIds.forEach(id -> {
            if (!productIds.contains(id)) {
                String errorMessage = "Товар c id =" + id + " не найден на складе";
                log.error(errorMessage);
                throw new ProductNotFoundInWarehouseException(errorMessage);
            }
        });
        products.forEach((key, value) -> {
            long availableQuantity = warehouseProducts.get(key).getQuantity();
            if (availableQuantity < value) {
                String errorMessage = String.format("Недостаточно товара %s на складе (требуется: %d, доступно: %d)",
                        key, value, availableQuantity);
                log.error(errorMessage);
                throw new ProductInShoppingCartLowQuantityInWarehouseException(errorMessage);
            }
        });

        log.info("Все товары имеются в достаточном количестве");
        return calculateBookingDetails(warehouseProducts.values(), products);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressDto getWarehouseAddress() {
        log.info("Запрос адреса склада");
        String address = Address.CURRENT_ADDRESS;
        AddressDto addressDto = AddressDto.builder()
                .country(address)
                .city(address)
                .street(address)
                .house(address)
                .flat(address)
                .build();

        log.debug("Адрес: {}", addressDto);
        return addressDto;
    }

    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest deliveryRequest) {
        UUID orderId = deliveryRequest.getOrderId();
        UUID deliveryId = deliveryRequest.getDeliveryId();

        log.info("Отправка товаров заказа {} в доставку {}", orderId, deliveryId);

        OrderBooking booking = orderBookingRepository.findByOrderId(orderId).orElseThrow(
                () -> {
                    log.error("Товары {} не найдены на складе", orderId);
                    return new NoSpecifiedProductInWarehouseException("Отсутствует информации о товаре на складе.");
                });

        booking.setDeliveryId(deliveryRequest.getDeliveryId());

        log.info("Товары заказа {} в доставке {}", orderId, deliveryId);
    }

    @Override
    public void acceptReturn(Map<UUID, Long> products) {
        log.info("Прием возврата {} товаров", products.size());

        List<Warehouse> warehousesItems = warehouseRepository.findAllById(products.keySet());
        log.debug("Найдено {} позиций на складе для возврата", warehousesItems.size());

        for (Warehouse warehouse : warehousesItems) {
            UUID productId = warehouse.getProductId();
            Long returnQuantity = products.get(productId);
            warehouse.setQuantity(warehouse.getQuantity() + returnQuantity);
            log.debug("Возвращено {} единиц товара {}", returnQuantity, productId);
        }
        log.info("Возврат {} товаров успешно обработан", products.size());
    }

    @Override
    public BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest assemblyProductsForOrder) {
        UUID shoppingCartId = assemblyProductsForOrder.getShoppingCartId();
        log.info("Начало сборки товаров для заказа из корзины {}", shoppingCartId);
        OrderBooking booking = orderBookingRepository.findById(shoppingCartId).orElseThrow(
                () -> {
                    log.error("Корзина покупок {} не найдена", shoppingCartId);
                    return new RuntimeException(String.format("Shopping cart %s not found", shoppingCartId));
                });
        log.debug("Найдена корзина покупок: {}", shoppingCartId);

        Map<UUID, Long> productsInBooking = booking.getProducts();
        List<Warehouse> productsInWarehouse = warehouseRepository.findAllById(productsInBooking.keySet());

        log.info("Проверка наличия {} товаров на складе", productsInBooking.size());

        productsInWarehouse.forEach(warehouse -> {
            if (warehouse.getQuantity() < productsInBooking.get(warehouse.getProductId())) {
                throw new ProductInShoppingCartLowQuantityInWarehouseException("Ошибка, нет необходимого количества товара на складе.");
            }
        });
        for (Warehouse warehouse : productsInWarehouse) {
            warehouse.setQuantity(warehouse.getQuantity() - productsInBooking.get(warehouse.getProductId()));
        }
        booking.setOrderId(assemblyProductsForOrder.getOrderId());
        log.info("Товары успешно собраны для заказа {}", assemblyProductsForOrder.getOrderId());
        return orderBookingMapper.toBookedProductsDto(booking);
    }


    private BookedProductsDto calculateBookingDetails(Collection<Warehouse> productList,
                                                      Map<UUID, Long> cartProducts) {
        return BookedProductsDto.builder()
                .fragile(productList.stream().anyMatch(Warehouse::getFragile))
                .deliveryWeight(productList.stream()
                        .mapToDouble(p -> p.getWeight() * cartProducts.get(p.getProductId()))
                        .sum())
                .deliveryVolume(productList.stream()
                        .mapToDouble(p ->
                                p.getDimension().getWidth() * p.getDimension().getHeight() * p.getDimension().getDepth() * cartProducts.get(p.getProductId()))
                        .sum())
                .build();
    }
}
