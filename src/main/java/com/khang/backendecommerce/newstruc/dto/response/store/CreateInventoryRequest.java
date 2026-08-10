package com.khang.backendecommerce.newstruc.dto.response.store;

public record CreateInventoryRequest(
        String warehouseId,
        Integer quantity
) {
}
