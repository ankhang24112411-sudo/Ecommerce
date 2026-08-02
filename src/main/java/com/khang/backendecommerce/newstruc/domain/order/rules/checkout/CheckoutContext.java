package com.khang.backendecommerce.newstruc.domain.order.rules.checkout;

import com.khang.backendecommerce.newstruc.domain.order.dto.realtime.CheckoutItemSnapShot;
import com.khang.backendecommerce.newstruc.domain.order.dto.realtime.CheckoutSnapshot;
import com.khang.backendecommerce.newstruc.domain.user.entity.UserEntity;
import com.khang.backendecommerce.newstruc.entity.DeliveryFeeEntity;
import com.khang.backendecommerce.newstruc.entity.DiscountEntity;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public record CheckoutContext(
        String userId,
        UserEntity user,
        CheckoutSnapshot checkoutSnap,
        Map<String, InventoryEntity> inventories,
        Map<String, DeliveryFeeEntity> deliveryFees,
        DiscountEntity discount
) {

    public CheckoutContext {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(user);
        Objects.requireNonNull(checkoutSnap);
        Objects.requireNonNull(inventories);
        Objects.requireNonNull(deliveryFees);

        inventories = Map.copyOf(inventories);
        deliveryFees = Map.copyOf(deliveryFees);
    }

    public List<CheckoutItemSnapShot> items() {
        return checkoutSnap.items();
    }

    public Optional<DiscountEntity> optionalDiscount() {
        return Optional.ofNullable(discount);
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