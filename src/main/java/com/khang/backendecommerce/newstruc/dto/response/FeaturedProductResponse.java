package com.khang.backendecommerce.newstruc.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record FeaturedProductResponse(
        String id,
        String name,
        String primaryImageURL,
        BigDecimal unitPrice
//        String storeName
) {
}
