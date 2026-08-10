package com.khang.backendecommerce.newstruc.dto.response.store;

import com.khang.backendecommerce.infrastructure.common.enums.InventoryStatus;
import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record OrderItemInSubOrderResponse(
        String productName,
        int quantity,
        InventoryStatus inventoryStatus,
        BigDecimal totalAmount
) {

}
