package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.PageResponse;
import com.khang.backendecommerce.newstruc.dto.request.OrderSummaryRequest;
import com.khang.backendecommerce.newstruc.dto.response.BannerResponse;
import com.khang.backendecommerce.newstruc.dto.response.FeaturedCategoryResponse;
import com.khang.backendecommerce.newstruc.dto.response.FeaturedProductResponse;
import com.khang.backendecommerce.newstruc.dto.response.StoreFrontHomeResponse;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import com.khang.backendecommerce.newstruc.entity.UserEntity;
import com.khang.backendecommerce.newstruc.repo.ProductRepository;
import com.khang.backendecommerce.newstruc.repo.specification.ProductSpecificationsBuilder;
import com.khang.backendecommerce.newstruc.repo.specification.SearchRepository;
import com.khang.backendecommerce.newstruc.service.ProductService;
import com.khang.backendecommerce.newstruc.service.StoreFrontService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.khang.backendecommerce.infrastructure.util.AppConst.SEARCH_SPEC_OPERATOR;

@Service
@Slf4j(topic = "STORE-FRONT-SERVICE")
@RequiredArgsConstructor
public class StoreFrontServiceImpl implements StoreFrontService {
    private final ProductService productService;
    private final ProductRepository productRepo;
    private final SearchRepository searchRepository;

    @Override
    public StoreFrontHomeResponse getStoreFront(OrderSummaryRequest orderSummaryRequest) {
        List<FeaturedProductResponse> product = productService.getFeaturedProduct();
        List<FeaturedCategoryResponse> categoryList = productService.getFeaturedCategory();
        List<BannerResponse> bannerResponseList = productService.getBanner();
        return StoreFrontHomeResponse.builder()
                .bannerResponseList(bannerResponseList)
                .productResponseList(product)
                .categoryResponseList(categoryList).build();
    }

    @Override
    public BaseResponse<?> advanceSearchWithSpecificationsProduct(Pageable pageable, String[] product, String[] store, String[] inventory) {
        if (store != null || inventory != null) {
            return searchRepository.searchProductByCriteriaWithJoin(pageable, product, store, inventory);
        }
        if (product != null) {

            ProductSpecificationsBuilder builder = new ProductSpecificationsBuilder();

            Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);


            for (String s : product) {
                Matcher matcher = pattern.matcher(s);

                if (matcher.find()) {
                    builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
                }
            }
            Page<ProductEntity> products = productRepo.findAll(Objects.requireNonNull(builder.build()), pageable);
            return BaseResponse.ofSuccess(PageResponse.of(products.getContent(), pageable, products.getTotalElements())
            );
        }


        Page<ProductEntity> products = productRepo.findAll(pageable);
        return BaseResponse.ofSuccess(PageResponse.of(products.getContent(), pageable, products.getTotalElements()));
    }

    public BaseResponse<?> advanceSearchWithSpecificationsProduct(Pageable pageable, String[] product, String[] store) {
        if (product != null && store != null) {
//            return searchRepository.searchProductByCriteriaWithJoin(pageable, product, store);

        }
        if (product != null) {
            ProductSpecificationsBuilder builder = new ProductSpecificationsBuilder();

            Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
            for (String s : product) {
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
                }
            }
            Page<ProductEntity> products = productRepo.findAll(Objects.requireNonNull(builder.build()), pageable);
        }
        return null;
    }


}
