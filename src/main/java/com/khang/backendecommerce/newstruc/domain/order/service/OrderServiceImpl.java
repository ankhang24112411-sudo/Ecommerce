package com.khang.backendecommerce.newstruc.domain.order.service;

import com.khang.backendecommerce.infrastructure.configuration.CurrentUserProvider;
import com.khang.backendecommerce.newstruc.domain.order.dto.request.OrderRequest;
import com.khang.backendecommerce.newstruc.domain.order.dto.response.OrderResponse;
import com.khang.backendecommerce.newstruc.domain.user.entity.UserEntity;
import com.khang.backendecommerce.newstruc.entity.CartItemEntity;
import com.khang.backendecommerce.newstruc.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ORDER - SERVICE")
public class OrderServiceImpl implements OrderService{
    private final CartService cartService;
    private final CurrentUserProvider currentUserProvider;
    @Override
    public OrderResponse placeOrder(OrderRequest request) {
        UserEntity user = currentUserProvider.getCurrentUser();
       List<CartItemEntity> cartItemList = cartService.loadCartItems(user);
        return null;
    }
}
