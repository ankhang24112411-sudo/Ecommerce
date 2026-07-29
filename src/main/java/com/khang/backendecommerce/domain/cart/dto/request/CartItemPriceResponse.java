package com.khang.backendecommerce.domain.cart.dto.request;

import com.khang.backendecommerce.infrastructure.common.enums.InventoryStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CartItemPriceResponse {
    private String image;
    private BigDecimal subtotal;
    private Integer quantity;
    private InventoryStatus inventoryStatus;
}
