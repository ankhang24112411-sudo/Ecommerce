package com.khang.backendecommerce.domain.discount.discountpattern.impl;

import com.khang.backendecommerce.domain.discount.discountpattern.DiscountStrategy;

import java.math.BigDecimal;

public class FreeshipDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal calculate(BigDecimal deliveryAmount, BigDecimal discountValue) {
       return deliveryAmount.multiply(discountValue).divide(BigDecimal.valueOf(100));
    }
}
