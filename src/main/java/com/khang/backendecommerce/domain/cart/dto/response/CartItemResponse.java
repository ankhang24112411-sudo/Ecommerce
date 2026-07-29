package com.khang.backendecommerce.domain.cart.dto.response;

import com.khang.backendecommerce.infrastructure.common.enums.InventoryStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CartItemResponse {
    private String image;
    private String name ;
    private BigDecimal subtotal;
    private Integer quantity;
    private InventoryStatus inventoryStatus;

}
