package com.khang.backendecommerce.domain.ordersummary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryResponse {
    private BigDecimal subtotal;
    private BigDecimal discountAmount;

    private BigDecimal deliveryAmount;
    private BigDecimal totalAmount;
}
