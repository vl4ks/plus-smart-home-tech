package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.iteractionapi.dto.BookedProductsDto;
import ru.yandex.practicum.iteractionapi.dto.DeliveryDto;
import ru.yandex.practicum.iteractionapi.dto.OrderDto;
import ru.yandex.practicum.iteractionapi.enums.OrderState;
import ru.yandex.practicum.iteractionapi.exception.NotAuthorizedUserException;
import ru.yandex.practicum.iteractionapi.feign.DeliveryClient;
import ru.yandex.practicum.iteractionapi.feign.PaymentClient;
import ru.yandex.practicum.iteractionapi.feign.WarehouseClient;
import ru.yandex.practicum.iteractionapi.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.iteractionapi.request.CreateNewOrderRequest;
import ru.yandex.practicum.iteractionapi.request.ProductReturnRequest;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final WarehouseClient warehouseClient;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;

    private final String STRING_ORDER_NOT_FOUND = "Заказ не найден.";

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrders(String username) {
        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым.");
        }
        log.info("Получение заказов для пользователя: {}", username);
        return orderRepository.findAllByUsername(username).stream()
                .map(OrderMapper.INSTANCE::toDto)
                .toList();
    }

    @Override
    public OrderDto createNewOrder(CreateNewOrderRequest newOrderRequest) {
        log.info("Создание нового заказа для корзины: {}", newOrderRequest.getShoppingCart().getShoppingCartId());
        Order order = Order.builder()
                .shoppingCartId(newOrderRequest.getShoppingCart().getShoppingCartId())
                .products(newOrderRequest.getShoppingCart().getProducts())
                .state(OrderState.NEW)
                .build();
        Order newOrder = orderRepository.save(order);
        log.debug("Создан новый заказ с id=: {}", newOrder.getOrderId());

        BookedProductsDto bookedProducts = warehouseClient.assemblyProductForOrder(
                new AssemblyProductsForOrderRequest(
                        newOrderRequest.getShoppingCart().getShoppingCartId(),
                        newOrder.getOrderId()
                ));
        log.debug("Получена информация о забронированных товарах для заказа {}", newOrder.getOrderId());

        newOrder.setFragile(bookedProducts.getFragile());
        newOrder.setDeliveryVolume(bookedProducts.getDeliveryVolume());
        newOrder.setDeliveryWeight(bookedProducts.getDeliveryWeight());
        newOrder.setProductPrice(paymentClient.productCost(orderMapper.toOrderDto(newOrder)));

        DeliveryDto deliveryDto = DeliveryDto.builder()
                .orderId(newOrder.getOrderId())
                .fromAddress(warehouseClient.getWarehouseAddress())
                .toAddress(newOrderRequest.getDeliveryAddress())
                .build();
        newOrder.setDeliveryId(deliveryClient.planDelivery(deliveryDto).getDeliveryId());

        paymentClient.payment(orderMapper.toOrderDto(newOrder));

        log.info("Успешно создан новый заказ c id=: {}", newOrder.getOrderId());
        return orderMapper.toOrderDto(newOrder);
    }

    @Override
    public OrderDto returnProduct(ProductReturnRequest returnRequest) {
        log.info("Обработка возврата товаров для заказа {}", returnRequest.getOrderId());
        Order order = orderRepository.findById(returnRequest.getOrderId())
                .orElseThrow(() -> new NoOrderFoundException(STRING_ORDER_NOT_FOUND));
        warehouseClient.acceptReturn(returnRequest.getProducts());
        order.setState(OrderState.PRODUCT_RETURNED);
        log.info("Товары по заказу {} успешно возвращены", order.getOrderId());
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto payment(UUID orderId) {
        log.info("Обработка оплаты для заказа {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException(STRING_ORDER_NOT_FOUND));
        order.setState(OrderState.PAID);
        log.info("Заказ {} успешно переведен в статус PAID", orderId);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto paymentFailed(UUID orderId) {
        log.info("Ошибка оплаты заказа {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException(STRING_ORDER_NOT_FOUND));
        order.setState(OrderState.PAYMENT_FAILED);
        log.warn("Заказ {} переведен в статус PAYMENT_FAILED", orderId);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto delivery(UUID orderId) {
        log.info("Доставка заказа {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException(STRING_ORDER_NOT_FOUND));
        order.setState(OrderState.DELIVERED);
        log.info("Заказ {} успешно доставлен", orderId);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto deliveryFailed(UUID orderId) {
        log.info("Ошибка доставки заказа {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException(STRING_ORDER_NOT_FOUND));
        order.setState(OrderState.DELIVERY_FAILED);
        log.warn("Заказ {} переведен в статус DELIVERY_FAILED", orderId);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto complete(UUID orderId) {
        log.info("Завершение заказа {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException(STRING_ORDER_NOT_FOUND));
        order.setState(OrderState.COMPLETED);
        log.info("Заказ {} успешно завершен", orderId);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto calculateTotalCost(UUID orderId) {
        log.debug("Расчет общей стоимости заказа {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException(STRING_ORDER_NOT_FOUND));
        order.setTotalPrice(paymentClient.getTotalCost(orderMapper.toOrderDto(order)));
        log.debug("Для заказа {} рассчитана общая стоимость: {}", orderId, order.getTotalPrice());
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID orderId) {
        log.debug("Расчет стоимости доставки заказа {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException(STRING_ORDER_NOT_FOUND));
        order.setDeliveryPrice(deliveryClient.deliveryCost(orderMapper.toOrderDto(order)));
        log.debug("Для заказа {} рассчитана стоимость доставки: {}", orderId, order.getDeliveryPrice());
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto assembly(UUID orderId) {
        log.info("Сборка заказа {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException(STRING_ORDER_NOT_FOUND));
        order.setState(OrderState.ASSEMBLED);
        log.info("Заказ {} успешно собран", orderId);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto assemblyFailed(UUID orderId) {
        log.info("Ошибка сборки заказа {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException(STRING_ORDER_NOT_FOUND));
        order.setState(OrderState.ASSEMBLY_FAILED);
        log.warn("Заказ {} переведен в статус ASSEMBLY_FAILED", orderId);
        return orderMapper.toOrderDto(order);
    }
}
