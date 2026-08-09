package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.newstruc.entity.CartEntity;
import com.khang.backendecommerce.infrastructure.discountinfra.DiscountContext;
import com.khang.backendecommerce.newstruc.entity.DiscountCustomerEntity;
import com.khang.backendecommerce.newstruc.entity.DiscountEntity;

import java.math.BigDecimal;

public interface DiscountService {
   DiscountCustomerEntity checkDiscountValidationFromUser(String userId , String voucherCode);
   void checkDateDiscount(DiscountEntity discount);
//    BigDecimal discountFreeShipCalculation(DiscountCustomerEntity discount, BigDecimal deliveryAmount);
//     BigDecimal discountCalculation(DiscountCustomerEntity discount,BigDecimal totalAmount );
     BigDecimal calculateDiscount(DiscountCustomerEntity discount ,  DiscountContext context );
    boolean isSameDiscount(CartEntity cart, String discountName);

    DiscountEntity findAndCheckDiscountCustomer(DiscountCustomerEntity discount);

    void clearDiscountCustomer(DiscountCustomerEntity discountCustomer);
}
