package com.khang.backendecommerce.newstruc.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemQuantityUpdate {
    @NotNull
    @Min(1)
    private int quantity;
}
