package com.khang.backendecommerce.newstruc.dto.request;

import com.khang.backendecommerce.infrastructure.common.enums.OrderSummarySource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryRequest {
    OrderSummarySource orderSummarySource;
    String productId;
    String discountName;
}
