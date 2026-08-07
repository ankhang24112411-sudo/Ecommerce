package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.newstruc.dto.request.CartItemPriceResponse;
import com.khang.backendecommerce.newstruc.dto.response.CartItemResponse;
import com.khang.backendecommerce.newstruc.entity.CartEntity;
import com.khang.backendecommerce.newstruc.dto.response.OrderSummaryResponse;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import com.khang.backendecommerce.newstruc.entity.UserEntity;
import com.khang.backendecommerce.newstruc.entity.CartItemEntity;

import java.util.List;

public interface CartService {
   List<CartItemResponse> getAllCartItems();

   CartItemPriceResponse updateCartItemQuantity(String itemId, int quantity);

   String deleteCartItems(String itemId);

   OrderSummaryResponse createBuyNow(UserEntity user, String discountName, String productId);

    OrderSummaryResponse getOrderSummaryResponseForBuyNow(UserEntity user, String discountName , CartEntity cart ) ;

    OrderSummaryResponse getOrderSummaryResponse(UserEntity user  , CartEntity cart, ProductEntity product, int quantity) ;

      CartEntity findByUserId(String id);

   List<CartItemEntity> loadCartItems(CartEntity cart, UserEntity user );
   }
