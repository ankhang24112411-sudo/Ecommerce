package com.khang.backendecommerce.infrastructure.discountinfra;

import com.khang.backendecommerce.newstruc.entity.DiscountEntity;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculate(DiscountEntity discount , DiscountContext context);
}
