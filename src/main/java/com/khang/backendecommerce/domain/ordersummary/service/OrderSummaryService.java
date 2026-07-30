package com.khang.backendecommerce.domain.ordersummary.service;

import com.khang.backendecommerce.domain.ordersummary.dto.request.OrderSummaryRequest;
import com.khang.backendecommerce.domain.ordersummary.dto.response.OrderSummaryResponse;

public interface OrderSummaryService {
    OrderSummaryResponse createOrderSummaryRequest(OrderSummaryRequest orderSummaryRequest);
}
