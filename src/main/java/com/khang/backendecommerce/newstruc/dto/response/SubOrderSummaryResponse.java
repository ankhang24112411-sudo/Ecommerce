package com.khang.backendecommerce.newstruc.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SubOrderSummaryResponse(
        BigDecimal deliveryFee,
        BigDecimal subtotal
) {
}

