package com.khang.backendecommerce.domain.product.service.impl;

import com.khang.backendecommerce.domain.product.entity.ProductEntity;
import com.khang.backendecommerce.domain.product.repository.ProductRepository;
import com.khang.backendecommerce.domain.product.service.ProductService;
import com.khang.backendecommerce.infrastructure.exception.RessourceNotFoundException;
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
            throw new RessourceNotFoundException("The product was deleted");
        }
    }

    @Override
    public ProductEntity findProductById(String productId) {
        return productRepo.findById(productId).orElseThrow(() -> new RessourceNotFoundException("Can not find product"));
    }
}
