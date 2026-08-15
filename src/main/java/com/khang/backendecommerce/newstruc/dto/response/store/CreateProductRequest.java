package com.khang.backendecommerce.newstruc.dto.response.store;

import com.khang.backendecommerce.newstruc.dto.request.ProductImageRequest;
import com.khang.backendecommerce.newstruc.entity.ProductImageEntity;
import com.khang.backendecommerce.newstruc.repo.ProductImageRepository;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductRequest(
        String name,
        String sku,
        String categoryId,
        BigDecimal price,
        String description,
        List<ProductImageRequest> images,
        List<CreateInventoryRequest> inventories
) {
}