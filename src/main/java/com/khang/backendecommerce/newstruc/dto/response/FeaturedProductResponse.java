package com.khang.backendecommerce.newstruc.dto.response;

import lombok.Builder;

@Builder
public record FeaturedProductResponse(
        String id,
        String name,
        String primaryImageURL
//        String storeName
) {
}
