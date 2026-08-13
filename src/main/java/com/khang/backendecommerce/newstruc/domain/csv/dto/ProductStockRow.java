package com.khang.backendecommerce.newstruc.domain.csv.dto;

import java.math.BigDecimal;

public record ProductStockRow (
        String productId,
        String name,
        String sku,
        BigDecimal price ,
        Long stockQuantity
){
}
