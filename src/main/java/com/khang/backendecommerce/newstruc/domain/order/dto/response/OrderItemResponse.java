package com.khang.backendecommerce.newstruc.domain.order.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemResponse(String orderItemId,
                                String productName,
                                String sku,
                                BigDecimal unitPrice,
                                Integer quantity,
                                BigDecimal lineTotal) {
}
