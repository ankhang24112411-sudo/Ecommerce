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
    public CartItemResponse(String image, String name, BigDecimal subtotal, Integer quantity, String inventoryStatus) {
        this.image = image;
        this.name = name;
        this.subtotal = subtotal;
        this.quantity = quantity;
        this.inventoryStatus = InventoryStatus.valueOf(inventoryStatus);
    }



}
