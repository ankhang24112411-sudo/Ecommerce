package com.khang.backendecommerce.newstruc.domain.order.dto.response;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.infrastructure.common.enums.PaymentStatus;
import com.khang.backendecommerce.newstruc.entity.OrderItem;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
public record OrderResponse (

    String orderCode,
    OrderStatus orderStatus,
    PaymentStatus paymentStatus,
    BigDecimal subtotal,
    BigDecimal deliveryAmount,
    BigDecimal discountAmount,
    BigDecimal totalAmount,

    Instant createdAt,
    List<SubOrderResponse> subOrders

)
{}
