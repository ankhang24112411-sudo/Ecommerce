package com.khang.backendecommerce.newstruc.dto.response;

import jakarta.persistence.Column;
import lombok.Builder;

@Builder
public record TrackingSubOrderResponse(

        String orderCode,
         String trackingCode,

 String message,

 String location

) {
}
