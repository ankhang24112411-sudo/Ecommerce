package com.khang.backendecommerce.newstruc.dto.response;

import lombok.Builder;

@Builder
public record BannerResponse(
        String id,
        String title,
        String description,
        String imageUrl,
        String buttonText,
        String targetUrl
) {
}
