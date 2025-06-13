package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.NoDeliveryFoundException;
import ru.yandex.practicum.iteractionapi.dto.DeliveryDto;
import ru.yandex.practicum.iteractionapi.dto.OrderDto;
import ru.yandex.practicum.iteractionapi.enums.DeliveryState;
import ru.yandex.practicum.iteractionapi.feign.OrderClient;
import ru.yandex.practicum.iteractionapi.feign.WarehouseClient;
import ru.yandex.practicum.iteractionapi.request.ShippedToDeliveryRequest;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.repository.DeliveryRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(5.0);
    private static final BigDecimal WAREHOUSE_1_ADDRESS_MULTIPLIER = BigDecimal.valueOf(1);
    private static final BigDecimal WAREHOUSE_2_ADDRESS_MULTIPLIER = BigDecimal.valueOf(2);
    private static final BigDecimal FRAGILE_SURCHARGE = BigDecimal.valueOf(0.2);
    private static final BigDecimal WEIGHT_RATE = BigDecimal.valueOf(0.3);
    private static final BigDecimal VOLUME_RATE = BigDecimal.valueOf(0.2);
    private static final BigDecimal DIFFERENT_STREET_SURCHARGE = BigDecimal.valueOf(0.2);


    @Override
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        log.info("Создание доставки для заказа {}", deliveryDto.getOrderId());
        Delivery delivery = deliveryMapper.toDelivery(deliveryDto);
        delivery.setDeliveryState(DeliveryState.CREATED);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        log.info("Доставка создана с id=: {}", savedDelivery.getDeliveryId());
        return deliveryMapper.toDeliveryDto(savedDelivery);
    }

    @Override
    public void deliverySuccessful(UUID deliveryId) {
        log.info("Эмуляция успешной доставки {} товара.", deliveryId);
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new NoDeliveryFoundException("Доставка не найдена"));
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        orderClient.complete(delivery.getOrderId());
        log.info("Доставка {} успешно завершена", deliveryId);
    }

    @Override
    public void deliveryPicked(UUID deliveryId) {
        log.info("Эмуляция получения товара в доставку {}.", deliveryId);
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new NoDeliveryFoundException("Не найдена доставка для выдачи"));
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        orderClient.assembly(delivery.getOrderId());
        ShippedToDeliveryRequest deliveryRequest = new ShippedToDeliveryRequest(
                delivery.getOrderId(), delivery.getDeliveryId());
        warehouseClient.shippedToDelivery(deliveryRequest);
        log.info("Доставка {} принята в работу", deliveryId);
    }

    @Override
    public void deliveryFailed(UUID deliveryId) {
        log.info("Эмуляция неудачного вручения доставки {} .", deliveryId);
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new NoDeliveryFoundException("Доставка не найдена"));
        delivery.setDeliveryState(DeliveryState.FAILED);
        orderClient.assemblyFailed(delivery.getOrderId());
        log.info("Доставка {} отмечена как неудачная", deliveryId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal deliveryCost(OrderDto orderDto) {
        log.info("Расчёт стоимости доставки для заказа {}", orderDto.getOrderId());
        Delivery delivery = deliveryRepository.findByOrderId(orderDto.getOrderId()).orElseThrow(
                () -> new NoDeliveryFoundException("Не найдена доставка для расчёта"));

        Address warehouseAddress = delivery.getFromAddress();
        Address finalAddress = delivery.getToAddress();

        BigDecimal addressCost = BASE_RATE;

        if (warehouseAddress.getCity().equals("ADDRESS_1")) {
            addressCost = addressCost.multiply(WAREHOUSE_1_ADDRESS_MULTIPLIER);
        } else {
            addressCost = addressCost.multiply(WAREHOUSE_2_ADDRESS_MULTIPLIER);
        }

        if (Boolean.TRUE.equals(orderDto.getFragile())) {
            addressCost = addressCost.add(addressCost.multiply(FRAGILE_SURCHARGE));
        }

        if (orderDto.getDeliveryWeight() != null) {
            BigDecimal weightCost = BigDecimal.valueOf(orderDto.getDeliveryWeight()).multiply(WEIGHT_RATE);
            addressCost = addressCost.add(weightCost);
        }

        if (orderDto.getDeliveryVolume() != null) {
            BigDecimal volumeCost = BigDecimal.valueOf(orderDto.getDeliveryVolume()).multiply(VOLUME_RATE);
            addressCost = addressCost.add(volumeCost);
        }

        if (!warehouseAddress.getStreet().equals(finalAddress.getStreet())) {
            addressCost = addressCost.add(addressCost.multiply(DIFFERENT_STREET_SURCHARGE));
        }

        log.info("Стоимость доставки для заказа {}: {}", orderDto.getOrderId(), addressCost);
        return addressCost;
    }
}
