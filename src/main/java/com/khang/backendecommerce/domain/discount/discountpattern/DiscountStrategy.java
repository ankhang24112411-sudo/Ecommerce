package com.khang.backendecommerce.domain.discount.discountpattern;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculate(BigDecimal subtotal , BigDecimal discountValue);
}
