package com.khang.backendecommerce.newstruc.dto.response.store;

import com.khang.backendecommerce.infrastructure.common.enums.InventoryStatus;
import com.khang.backendecommerce.infrastructure.common.enums.PaymentMethod;
import com.khang.backendecommerce.infrastructure.common.enums.PaymentStatus;
import com.khang.backendecommerce.newstruc.domain.order.dto.response.OrderItemResponse;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record SubOrderPendingResponse (


    String orderId,
    String orderCode,

    Instant createdAt,

    String customerName,

    List<OrderItemInSubOrderResponse> items,

    String shippingAddress,


    BigDecimal totalAmount,

    PaymentMethod paymentMethod,
    PaymentStatus paymentStatus

    )
{
}
