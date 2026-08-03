package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import com.khang.backendecommerce.newstruc.repo.ProductRepository;
import com.khang.backendecommerce.newstruc.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "PRODUCT - SERVICE")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepo;
    @Override
    public void isProductActive(ProductEntity product) {
        if(product.getDeleted() == 1){
            throw ApplicationErrors.PRODUCT_INACTIVE;
        }
    }

    @Override
    public ProductEntity findProductById(String productId) {
        return productRepo.findById(productId).orElseThrow(() -> ApplicationErrors.PRODUCT_NOT_FOUND);
    }
}
