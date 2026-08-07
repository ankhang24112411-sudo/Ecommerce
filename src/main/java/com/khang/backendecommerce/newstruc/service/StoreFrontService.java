package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.newstruc.dto.request.OrderSummaryRequest;
import com.khang.backendecommerce.newstruc.dto.response.StoreFrontHomeResponse;

public interface StoreFrontService {
    StoreFrontHomeResponse getStoreFront(OrderSummaryRequest orderSummaryRequest);
}
