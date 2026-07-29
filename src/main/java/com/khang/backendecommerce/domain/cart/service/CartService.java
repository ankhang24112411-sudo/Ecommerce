package com.khang.backendecommerce.domain.cart.service;

import com.khang.backendecommerce.domain.cart.dto.response.CartItemResponse;

import java.util.List;

public interface CartService {
   List<CartItemResponse> getAllCartItems();

   CartItemResponse updateCartItemQuantity(String itemId, Integer quantity);
}
