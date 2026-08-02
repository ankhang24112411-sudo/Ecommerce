package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.newstruc.dto.request.OrderSummaryRequest;
import com.khang.backendecommerce.newstruc.dto.response.OrderSummaryResponse;

public interface OrderSummaryService {
    OrderSummaryResponse createOrderSummaryRequest(OrderSummaryRequest orderSummaryRequest);
}
