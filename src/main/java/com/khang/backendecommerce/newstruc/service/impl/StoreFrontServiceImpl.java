package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.newstruc.dto.request.OrderSummaryRequest;
import com.khang.backendecommerce.newstruc.dto.response.BannerResponse;
import com.khang.backendecommerce.newstruc.dto.response.FeaturedCategoryResponse;
import com.khang.backendecommerce.newstruc.dto.response.FeaturedProductResponse;
import com.khang.backendecommerce.newstruc.dto.response.StoreFrontHomeResponse;
import com.khang.backendecommerce.newstruc.service.ProductService;
import com.khang.backendecommerce.newstruc.service.StoreFrontService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j(topic= "STORE-FRONT-SERVICE")
@RequiredArgsConstructor
public class StoreFrontServiceImpl implements StoreFrontService {
    private final ProductService productService;
    @Override
    public StoreFrontHomeResponse getStoreFront(OrderSummaryRequest orderSummaryRequest) {
        List<FeaturedProductResponse> product = productService.getFeaturedProduct();
        List<FeaturedCategoryResponse> categoryList = productService.getFeaturedCategory();
        List<BannerResponse> bannerResponseList = productService.getBanner();
        return null;
    }
}
