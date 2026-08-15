package com.khang.backendecommerce.newstruc.dto.response;

import com.khang.backendecommerce.newstruc.entity.DeliveryFeeEntity;
import com.khang.backendecommerce.newstruc.entity.DeliveryRouteEntity;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;

import java.math.BigDecimal;

public record InventoryNewCartContext(
        InventoryEntity inventory,
        DeliveryFeeEntity deliveryFee
) {
}
