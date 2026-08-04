package com.khang.backendecommerce.newstruc.domain.order.dto;

import com.khang.backendecommerce.newstruc.entity.CartItemEntity;
import com.khang.backendecommerce.newstruc.entity.DeliveryRouteEntity;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Objects;
@Builder
public record AllocatedItem(CartItemEntity cartItem,
                            ProductEntity product,
                            InventoryEntity inventory,
                            int quantity,
                            BigDecimal unitPrice,
                            BigDecimal subtotal,
                            DeliveryRouteEntity deliveryRoute,
                            BigDecimal deliveryFee) {

    public AllocatedItem {

        Objects.requireNonNull(product);
        Objects.requireNonNull(inventory);
        Objects.requireNonNull(unitPrice);
        Objects.requireNonNull(deliveryFee);

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero"
            );
        }
    }

    public String productId() {
        return product.getId();
    }

//    public String shopId() {
//        return product.getShop().getId();
//    }

    public String warehouseId() {
        return inventory.getWarehouse().getId();
    }

    public BigDecimal subtotal() {
        return unitPrice.multiply(
                BigDecimal.valueOf(quantity)
        );
    }
}