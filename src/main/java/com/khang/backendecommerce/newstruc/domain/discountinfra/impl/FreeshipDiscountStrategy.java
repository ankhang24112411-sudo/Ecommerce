package com.khang.backendecommerce.newstruc.domain.discountinfra.impl;

import com.khang.backendecommerce.newstruc.domain.discountinfra.DiscountContext;
import com.khang.backendecommerce.newstruc.domain.discountinfra.DiscountStrategy;
import com.khang.backendecommerce.newstruc.entity.DiscountEntity;

import java.math.BigDecimal;

public class FreeshipDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal calculate(DiscountEntity discount, DiscountContext context) {
//       return deliveryAmount.multiply(discountValue).divide(BigDecimal.valueOf(100));
        return context.deliveryAmount().multiply(discount.getDiscountValue()).divide(BigDecimal.valueOf(100));
    }
}
