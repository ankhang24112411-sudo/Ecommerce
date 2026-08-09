package com.khang.backendecommerce.newstruc.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public class StoreFrontHomeResponse {
    List<BannerResponse> bannerResponseList;
    List<FeaturedProductResponse> productResponseList;
    List<FeaturedCategoryResponse> categoryResponseList;
}
