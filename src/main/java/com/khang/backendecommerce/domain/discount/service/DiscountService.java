package com.khang.backendecommerce.domain.discount.service;

import com.khang.backendecommerce.domain.discount.entity.DiscountCustomerEntity;
import com.khang.backendecommerce.domain.discount.entity.DiscountEntity;

import java.time.Instant;

public interface DiscountService {
   DiscountCustomerEntity checkDiscountValidationFromUser(String userId , String voucherCode);
   void checkDateDiscount(DiscountEntity discount);
}
