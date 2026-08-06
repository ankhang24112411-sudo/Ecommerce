package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.newstruc.entity.ProductEntity;

public interface ProductService {
    void isProductActive(ProductEntity product);

    ProductEntity findProductById(String productId);

     ProductEntity findProductByIdWithShop(String productId);
}
