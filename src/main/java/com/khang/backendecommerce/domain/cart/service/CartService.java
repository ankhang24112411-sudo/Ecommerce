package com.khang.backendecommerce.domain.cart.service;

import com.khang.backendecommerce.domain.cart.dto.request.CartItemPriceResponse;
import com.khang.backendecommerce.domain.cart.dto.response.CartItemResponse;
import com.khang.backendecommerce.domain.cart.entity.CartEntity;
import com.khang.backendecommerce.domain.discount.entity.DiscountCustomerEntity;
import com.khang.backendecommerce.domain.ordersummary.dto.response.OrderSummaryResponse;
import com.khang.backendecommerce.domain.user.entity.UserEntity;

import java.util.List;

public interface CartService {
   List<CartItemResponse> getAllCartItems();

   CartItemPriceResponse updateCartItemQuantity(String itemId, Integer quantity);

   String deleteCartItems(String itemId);

   OrderSummaryResponse createBuyNow(UserEntity user, DiscountCustomerEntity discount, String productId);

    OrderSummaryResponse convertToOrderSummaryResponse(UserEntity user , CartEntity cart ) ;

   CartEntity findByUserId(String id);
   }
