package com.khang.backendecommerce.domain.cart.service.impl;

import com.khang.backendecommerce.domain.cart.dto.response.CartItemResponse;
import com.khang.backendecommerce.domain.cart.repo.CartRepository;
import com.khang.backendecommerce.domain.cart.service.CartService;
import com.khang.backendecommerce.domain.user.entity.UserEntity;
import com.khang.backendecommerce.infrastructure.configuration.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final  CurrentUserProvider currentUserProvider;
    private final CartRepository cartRepo;
    @Override
    public List<CartItemResponse> getAllCartItems() {
        UserEntity user = currentUserProvider.getCurrentUser();

        return List.of();
    }
}
