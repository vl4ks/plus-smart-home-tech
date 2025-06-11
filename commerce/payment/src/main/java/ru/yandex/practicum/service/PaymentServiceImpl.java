package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.NoPaymentFoundException;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.iteractionapi.dto.OrderDto;
import ru.yandex.practicum.iteractionapi.dto.PaymentDto;
import ru.yandex.practicum.iteractionapi.dto.ProductDto;
import ru.yandex.practicum.iteractionapi.enums.PaymentState;
import ru.yandex.practicum.iteractionapi.feign.OrderClient;
import ru.yandex.practicum.iteractionapi.feign.ShoppingStoreClient;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.repository.PaymentRepository;

import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    private final String STRING_PAYMENT_NOT_FOUND = "Данная оплата не найдена.";

    @Override
    public PaymentDto payment(OrderDto orderDto) {
        log.info("Формирование платежа для заказа {}", orderDto.getOrderId());
        checkOrder(orderDto);
        Payment payment = PaymentMapper.INSTANCE.newPayment(orderDto);
        payment = paymentRepository.save(payment);
        log.info("Платеж успешно сформирован, id=: {}", payment.getOrderId());
        return PaymentMapper.INSTANCE.toPaymentDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalCost(OrderDto orderDto) {
        log.info("Расчет общей стоимости заказа {}", orderDto.getOrderId());
        if (orderDto.getDeliveryPrice() == null || orderDto.getProductPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Необходимо установить цену доставки и стоимость продукта");
        }
        double totalCost = orderDto.getProductPrice() * 1.1 + orderDto.getDeliveryPrice();
        log.info("Общая стоимость заказа {}: {}", orderDto.getOrderId(), totalCost);

        return totalCost;
    }

    @Override
    public void paymentSuccess(UUID uuid) {
        log.info("Обработка успешного платежа(эмуляция) {}", uuid);
        Payment payment = paymentRepository.findById(uuid).orElseThrow(
                () -> new NoPaymentFoundException(STRING_PAYMENT_NOT_FOUND));
        payment.setStatus(PaymentState.SUCCESS);
        paymentRepository.save(payment);
        orderClient.payment(payment.getOrderId());
        log.info("Платеж {} прошел успешно", uuid);
    }

    @Override
    public void paymentFailed(UUID uuid) {
        log.info("Обработка отказанного платежа {}", uuid);
        Payment payment = paymentRepository.findById(uuid).orElseThrow(
                () -> new NoPaymentFoundException(STRING_PAYMENT_NOT_FOUND));
        payment.setStatus(PaymentState.FAILED);
        paymentRepository.save(payment);
        orderClient.paymentFailed(payment.getOrderId());
        log.info("Платеж {} отказан", uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Double productCost(OrderDto orderDto) {
        log.info("Расчет стоимости товаров для заказа {}", orderDto.getOrderId());
        double productCost = 0.0;

        if (orderDto.getProducts() == null || orderDto.getProducts().isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException("Отсутствуют товары в заказе");
        }
        for (Map.Entry<UUID, Long> entry : orderDto.getProducts().entrySet()) {
            ProductDto product = shoppingStoreClient.getProduct(entry.getKey());
            productCost += product.getPrice() * entry.getValue();
        }
        log.info("Стоимость товаров для заказа {}: {}", orderDto.getOrderId(), productCost);
        return productCost;
    }

    private void checkOrder(OrderDto orderDto) {
        if (orderDto.getTotalPrice() == null || orderDto.getDeliveryPrice() == null || orderDto.getProductPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Необходимо указать общую цену, подтвердить стоимость доставки и цену продукта");
        }
    }

}
