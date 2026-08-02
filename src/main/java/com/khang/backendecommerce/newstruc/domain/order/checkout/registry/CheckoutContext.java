package com.khang.backendecommerce.newstruc.domain.order.checkout.registry;

import com.khang.backendecommerce.newstruc.domain.user.entity.UserEntity;
import com.khang.backendecommerce.newstruc.entity.DeliveryFeeEntity;
import com.khang.backendecommerce.newstruc.entity.DiscountEntity;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public record        CheckoutContext
(
        UserEntity user,
        List<CheckoutItemSnapshot> items,
        Map<String, InventoryEntity> inventories,
        Map<String, DeliveryFeeEntity> deliveryFees,
        DiscountEntity discount,
        int quantity

) {

    public CheckoutContext {
        Objects.requireNonNull(user);
        Objects.requireNonNull(inventories);
        Objects.requireNonNull(deliveryFees);

        inventories = Map.copyOf(inventories);
        deliveryFees = Map.copyOf(deliveryFees);
    }



    public Optional<DiscountEntity> optionalDiscount() {
        return Optional.ofNullable(discount);
    }
    public List<CheckoutItemSnapshot> items(){
        return items;
    }
    public InventoryEntity requireInventory(String productId) {
        InventoryEntity inventory = inventories.get(productId);

        if (inventory == null) {
            throw new IllegalStateException(
                    "Inventory was not loaded: " + productId
            );
        }
        return inventory;
    }
    public DeliveryFeeEntity requireDeliveryFee(String shopId) {
        DeliveryFeeEntity deliveryFee = deliveryFees.get(shopId);

        if (deliveryFee == null) {
            throw new IllegalStateException("Delivery fee was not loaded for shop: " + shopId);
        }
        return deliveryFee;
    }
}