package com.khang.backendecommerce.newstruc.dto.response;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import jakarta.persistence.Column;
import lombok.Builder;

import java.time.Instant;

@Builder
public record TrackingSubOrderResponse(

        String orderCode,
         String trackingCode,
OrderStatus status,

 Instant updatedAt,

 String message,

 String location

) {
}
