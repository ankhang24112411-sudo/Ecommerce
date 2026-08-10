package com.khang.backendecommerce.newstruc.dto.response.store;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record SubOrderStatusResponse(
        String subOrderId,
        OrderStatus orderStatus,
        Instant confirmedAt,
        Instant rejectedAt
) {}
