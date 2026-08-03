package com.khang.backendecommerce.newstruc.domain.order.service;

import com.khang.backendecommerce.newstruc.domain.order.dto.request.OrderRequest;
import com.khang.backendecommerce.newstruc.domain.order.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest request);
}
