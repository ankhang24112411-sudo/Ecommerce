package com.khang.backendecommerce.domain.ordersummary.dto.request;

import com.khang.backendecommerce.infrastructure.common.enums.OrderSummarySource;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderSummaryRequest {
    OrderSummarySource orderSummarySource;
    List<String> orderItems;
    String productId;
    Integer quantity;
    String voucherCode;
}
