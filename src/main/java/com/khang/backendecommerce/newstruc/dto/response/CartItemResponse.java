package com.khang.backendecommerce.newstruc.dto.response;

import com.khang.backendecommerce.infrastructure.common.enums.InventoryStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter

public class CartItemResponse {
    private String image;
    private String name ;
    private BigDecimal subtotal;
    private Integer quantity;
    private InventoryStatus inventoryStatus;
    @Builder
    public CartItemResponse(String image, String name, BigDecimal subtotal, Integer quantity, String inventoryStatus) {
        this.image = image;
        this.name = name;
        this.subtotal = subtotal;
        this.quantity = quantity;
        this.inventoryStatus = InventoryStatus.valueOf(inventoryStatus);
    }



}
