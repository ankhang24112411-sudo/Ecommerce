package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.newstruc.dto.response.BannerResponse;
import com.khang.backendecommerce.newstruc.dto.response.FeaturedCategoryResponse;
import com.khang.backendecommerce.newstruc.dto.response.FeaturedProductResponse;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;

import java.util.List;

public interface ProductService {
    void isProductActive(ProductEntity product);

    ProductEntity findProductById(String productId);

    ProductEntity findProductByIdWithShop(String productId);

    List<FeaturedProductResponse> getFeaturedProduct();

    List<FeaturedCategoryResponse> getFeaturedCategory();

    List<BannerResponse> getBanner();
}
