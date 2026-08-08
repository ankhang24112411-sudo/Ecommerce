package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import com.khang.backendecommerce.newstruc.dto.request.OrderSummaryRequest;
import com.khang.backendecommerce.newstruc.dto.response.StoreFrontHomeResponse;
import org.springframework.data.domain.Pageable;

public interface StoreFrontService {
    StoreFrontHomeResponse getStoreFront(OrderSummaryRequest orderSummaryRequest);

    BaseResponse<?> advanceSearchWithSpecificationsProduct(Pageable pageable, String[] user, String [] store);
}
