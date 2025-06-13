package ru.yandex.practicum.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.iteractionapi.dto.OrderDto;
import ru.yandex.practicum.iteractionapi.dto.PaymentDto;
import ru.yandex.practicum.iteractionapi.enums.PaymentState;
import ru.yandex.practicum.model.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    default Payment newPayment(OrderDto orderDto) {
        return Payment.builder()
                .orderId(orderDto.getOrderId())
                .deliveryTotal(orderDto.getDeliveryPrice())
                .feeTotal(orderDto.getProductPrice())
                .totalPayment(orderDto.getTotalPrice())
                .status(PaymentState.PENDING)
                .build();
    }

    PaymentDto toPaymentDto(Payment payment);
}
