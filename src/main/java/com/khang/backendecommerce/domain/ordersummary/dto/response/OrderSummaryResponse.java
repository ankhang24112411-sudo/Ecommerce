package com.khang.backendecommerce.domain.ordersummary.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class OrderSummaryResponse {
    private BigDecimal subtotal;
    private BigDecimal discountAmount;

    private BigDecimal deliveryAmount;
    private BigDecimal totalAmount;
}
