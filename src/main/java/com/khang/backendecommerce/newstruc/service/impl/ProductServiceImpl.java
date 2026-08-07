package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.newstruc.dto.response.FeaturedCategoryResponse;
import com.khang.backendecommerce.newstruc.dto.response.FeaturedProductResponse;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import com.khang.backendecommerce.newstruc.entity.ProductImageEntity;
import com.khang.backendecommerce.newstruc.repo.ProductImageRepository;
import com.khang.backendecommerce.newstruc.repo.ProductRepository;
import com.khang.backendecommerce.newstruc.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "PRODUCT - SERVICE")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepo;
    private final ProductImageRepository productImageRepo;
    private final CategoryRepository categoryRepo;
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

    @Override
    public ProductEntity findProductByIdWithShop(String productId) {
        return productRepo.findProductAndShopByProductId(productId).orElseThrow(() -> ApplicationErrors.PRODUCT_NOT_FOUND);
    }

    @Override
    public List<FeaturedProductResponse> getFeaturedProduct() {
        List<String> productIds = productRepo.getFeaturedProduct(PageRequest.of(0,5));
        Map<String, ProductImageEntity>  productImageEntityByProductId = productImageRepo.getFeaturedProductImage(productIds).stream()
                .collect(Collectors
                        .toMap(productImageEntity -> productImageEntity.getProduct().getId(), Function.identity()));
        return productImageEntityByProductId.entrySet().stream()
                .map( productImageEntityById -> {
                    ProductImageEntity productImage = productImageEntityById.getValue();
                           return FeaturedProductResponse.builder()
                            .id(productImageEntityById.getKey())
                                   .name(productImage.getProduct().getName())
                                   .primaryImageURL(productImage.getImage())
                            .build();
                       }).toList();




//                    FeaturedCategoryResponse response = FeaturedCategoryResponse.builder()
//                            .id().name().imageURL()
//                            .build();

    }

    @Override
    public List<FeaturedCategoryResponse> getFeaturedCategory() {
        List<String> featureCategoryIds = categoryRepo.
        return List.of();
    }
}
