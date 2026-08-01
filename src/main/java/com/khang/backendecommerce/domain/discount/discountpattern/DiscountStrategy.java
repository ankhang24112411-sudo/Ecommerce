package com.khang.backendecommerce.domain.discount.discountpattern;

import com.khang.backendecommerce.domain.discount.entity.DiscountEntity;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculate(DiscountEntity discount , DiscountContext context);
}
