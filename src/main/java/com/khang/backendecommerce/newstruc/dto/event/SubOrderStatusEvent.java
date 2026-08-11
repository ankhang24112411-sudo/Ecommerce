package com.khang.backendecommerce.newstruc.dto.event;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;

public record SubOrderStatusEvent(
        String orderId,
        String orderCode,

        String subOrderId,
        String subOrderCode,

        String trackingCode,

        String customerName,
        String customerEmail,

        OrderStatus status
) {
}