package com.khang.backendecommerce.newstruc.domain.order.dto.response;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record SubOrderResponse(
        String subOrderId,

        String storeName,

        OrderStatus status,

        BigDecimal subTotal,
        BigDecimal deliveryFee,

        List<OrderItemResponse> items
) {
}
