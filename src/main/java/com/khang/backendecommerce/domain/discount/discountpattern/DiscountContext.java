package com.khang.backendecommerce.domain.discount.discountpattern;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DiscountContext(
        BigDecimal subtotal ,
        BigDecimal deliveryAmount
) {
}
