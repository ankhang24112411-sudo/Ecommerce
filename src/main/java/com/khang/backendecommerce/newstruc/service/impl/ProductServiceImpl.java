package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.newstruc.dto.response.BannerResponse;
import com.khang.backendecommerce.newstruc.dto.response.FeaturedCategoryResponse;
import com.khang.backendecommerce.newstruc.dto.response.FeaturedProductResponse;
import com.khang.backendecommerce.newstruc.entity.CategoryEntity;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import com.khang.backendecommerce.newstruc.entity.ProductImageEntity;
import com.khang.backendecommerce.newstruc.repo.BannerRepository;
import com.khang.backendecommerce.newstruc.repo.CategoryRepository;
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
    private final BannerRepository bannerRepo;
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
        Map<String, ProductImageEntity>  productImageEntityByProductId = productImageRepo
                .getFeaturedProductImage(productIds).stream()
                .filter(productImageEntity -> productImageEntity.getPrimary() == 1)
                .collect(Collectors
                        .toMap(productImageEntity -> productImageEntity.getProduct().getId(), Function.identity()));


        return productImageEntityByProductId.entrySet().stream()
                .map( productImageEntityById -> {
                    ProductImageEntity productImage = productImageEntityById.getValue();

                    ProductEntity product = productRepo.findById(productImageEntityById.getKey()).orElseThrow(() -> ApplicationErrors.PRODUCT_NOT_FOUND);
                           return FeaturedProductResponse.builder()
                            .id(productImageEntityById.getKey())
                                   .name(productImage.getProduct().getName())
                                   .primaryImageURL(productImage.getImage())
                                   .unitPrice(product.getPrice())
                            .build();
                       }).toList();

    }

    @Override
    public List<FeaturedCategoryResponse> getFeaturedCategory() {
        List<CategoryEntity> featureCategory = categoryRepo.getFeaturedCategory(PageRequest.of(0,5));
        return featureCategory.stream()
                .map(categoryEntity -> FeaturedCategoryResponse.builder()
                        .id(categoryEntity.getId())
                        .name(categoryEntity.getName())
                        .imageURL(categoryEntity.getImageUrl()).build())
                .toList();
    }

    @Override
    public List<BannerResponse> getBanner() {
        return bannerRepo.getBanner(PageRequest.of(0,5))
                .stream()
                .map(banner -> BannerResponse.builder()
                        .id(banner.getId())
                        .title(banner.getTitle())
                        .description(banner.getDescription())
                        .imageUrl(banner.getImageUrl())
                        .buttonText(banner.getButtonText())
                        .targetUrl(banner.getTargetUrl())
                        .build())
                .toList();

    }
}
