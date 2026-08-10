package com.khang.backendecommerce.newstruc.dto.response.store;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductRequest(
        String name,
        String sku,
        String categoryId,
        BigDecimal price,
        String description,

        List<CreateInventoryRequest> inventories
) {}