package com.khang.backendecommerce.newstruc.domain.discountinfra;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DiscountContext(
        BigDecimal subtotal ,
        BigDecimal deliveryAmount
) {
}
