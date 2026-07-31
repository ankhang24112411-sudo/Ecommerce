package com.khang.backendecommerce.domain.discount.service;

import com.khang.backendecommerce.domain.discount.entity.DiscountCustomerEntity;
import com.khang.backendecommerce.domain.discount.entity.DiscountEntity;

import java.math.BigDecimal;
import java.time.Instant;

public interface DiscountService {
   DiscountCustomerEntity checkDiscountValidationFromUser(String userId , String voucherCode);
   void checkDateDiscount(DiscountEntity discount);
    BigDecimal discountFreeShipCalculation(DiscountCustomerEntity discount, BigDecimal deliveryAmount);
     BigDecimal discountCalculation(DiscountCustomerEntity discount,BigDecimal totalAmount );
     BigDecimal calculateDiscount(DiscountCustomerEntity discount , BigDecimal totalAmount );
   }
