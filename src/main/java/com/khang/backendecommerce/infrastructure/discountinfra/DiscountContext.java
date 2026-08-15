package com.khang.backendecommerce.infrastructure.discountinfra;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DiscountContext(
        BigDecimal subtotal,
        BigDecimal deliveryAmount
) {
}
