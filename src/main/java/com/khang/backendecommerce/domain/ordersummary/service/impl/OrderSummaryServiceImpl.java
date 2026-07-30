package com.khang.backendecommerce.domain.ordersummary.service.impl;

import com.khang.backendecommerce.domain.cart.service.CartService;
import com.khang.backendecommerce.domain.discount.entity.DiscountCustomerEntity;
import com.khang.backendecommerce.domain.discount.service.DiscountService;
import com.khang.backendecommerce.domain.ordersummary.dto.request.OrderSummaryRequest;
import com.khang.backendecommerce.domain.ordersummary.dto.response.OrderSummaryResponse;
import com.khang.backendecommerce.domain.ordersummary.service.OrderSummaryService;
import com.khang.backendecommerce.domain.user.entity.UserEntity;
import com.khang.backendecommerce.infrastructure.configuration.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "ORDER - SUMMARY - SERVICE")
@RequiredArgsConstructor
public class OrderSummaryServiceImpl implements OrderSummaryService {
    private final CurrentUserProvider currentUserProvider;
    private final DiscountService discountService;
    private final CartService cartService;
    @Override
    public OrderSummaryResponse createOrderSummaryRequest(OrderSummaryRequest orderSummaryRequest) {
        DiscountCustomerEntity discount = null;
        UserEntity user = currentUserProvider.getCurrentUser();
        if(orderSummaryRequest.getDiscountName() != null){
          discount = discountService.checkDiscountValidationFromUser(user.getId(), orderSummaryRequest.getDiscountName());
        }
        final var orderSource = orderSummaryRequest.getOrderSummarySource();
        switch (orderSource){
            case BUY_NOW -> cartService.createBuyNow(user,discount,orderSummaryRequest.getProductId());
        }
        return null;
    }

}
