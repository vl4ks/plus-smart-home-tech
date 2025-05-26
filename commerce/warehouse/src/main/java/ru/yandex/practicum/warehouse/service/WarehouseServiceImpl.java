package ru.yandex.practicum.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.iteractionapi.dto.AddressDto;
import ru.yandex.practicum.iteractionapi.dto.BookedProductsDto;
import ru.yandex.practicum.iteractionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.iteractionapi.enums.QuantityState;
import ru.yandex.practicum.iteractionapi.feign.ShoppingStoreClient;
import ru.yandex.practicum.iteractionapi.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.iteractionapi.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.warehouse.address.Address;
import ru.yandex.practicum.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.warehouse.exception.ProductNotFoundInWarehouseException;
import ru.yandex.practicum.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.warehouse.mapper.WarehouseMapper;
import ru.yandex.practicum.warehouse.model.Warehouse;
import ru.yandex.practicum.warehouse.repository.WarehouseRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.READ_COMMITTED)
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final ShoppingStoreClient shoppingStoreClient;

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
            warehouse.setQuantity(0);
        }
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        log.info("Товар успешно добавлен на склад. id: {}, Количество: {}",
                savedWarehouse.getProductId(),
                savedWarehouse.getQuantity());
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest addProductToWarehouseRequest) {
        log.info("Добавление товара на склад. id товара: {}, Количество: {}",
                addProductToWarehouseRequest.getProductId(),
                addProductToWarehouseRequest.getQuantity());

        Warehouse warehouse = warehouseRepository.findById(addProductToWarehouseRequest.getProductId())
                .orElseThrow(() -> {
                    String errorMessage = "Товар c id =" + addProductToWarehouseRequest.getProductId() + " не найден на складе";
                    log.error(errorMessage);
                    return new NoSpecifiedProductInWarehouseException(errorMessage);
                });

        Integer oldQuantity = warehouse.getQuantity();
        warehouse.setQuantity(oldQuantity + addProductToWarehouseRequest.getQuantity());
        warehouseRepository.save(warehouse);

        log.info("Количество товара обновлено. id = : {}, Было: {}, Стало: {}",
                warehouse.getProductId(),
                oldQuantity,
                warehouse.getQuantity());

        syncProductStoreStatus(warehouse);
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
    public AddressDto fetchWarehouseAddress() {
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

    private void syncProductStoreStatus(Warehouse warehouseProduct) {
        UUID productId = warehouseProduct.getProductId();
        QuantityState quantityState;
        Integer quantity = warehouseProduct.getQuantity();

        if (quantity == 0) {
            quantityState = QuantityState.ENDED;
        } else if (quantity < 10) {
            quantityState = QuantityState.ENOUGH;
        } else if (quantity < 100) {
            quantityState = QuantityState.FEW;
        } else {
            quantityState = QuantityState.MANY;
        }
        shoppingStoreClient.setProductQuantityState(productId, quantityState);
    }

}
