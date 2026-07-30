package com.khang.backendecommerce.domain.product.service;

import com.khang.backendecommerce.domain.product.entity.ProductEntity;

public interface ProductService {
    void isProductActive(ProductEntity product);

    ProductEntity findProductById(String productId);
}
