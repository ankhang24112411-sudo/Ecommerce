package com.khang.backendecommerce.newstruc.dto.response;

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
