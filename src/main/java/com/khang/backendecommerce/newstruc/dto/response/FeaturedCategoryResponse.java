package com.khang.backendecommerce.newstruc.dto.response;

import lombok.Builder;

@Builder
public record FeaturedCategoryResponse(
        String id,
        String name,
        String imageURL
) {
}
