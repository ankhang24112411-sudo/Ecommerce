package com.khang.backendecommerce.newstruc.entity;

public record SubOrderGroupKey(
        String storeId,
        String warehouseId,
        String deliveryRouteId
) {
}
