package com.khang.backendecommerce.newstruc.domain.order.rules.rule;

import com.khang.backendecommerce.newstruc.domain.order.checkout.registry.CheckoutContext;
import com.khang.backendecommerce.newstruc.domain.order.checkout.registry.CheckoutItemSnapshot;
import com.khang.backendecommerce.newstruc.domain.order.rules.checkout.CheckoutRule;
import com.khang.backendecommerce.newstruc.domain.order.rules.checkout.CheckoutViolation;
import com.khang.backendecommerce.newstruc.domain.order.rules.checkout.task.CheckoutPreviewRule;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;

import java.util.ArrayList;
import java.util.List;

public class InventoryAvailableRule implements CheckoutRule, CheckoutPreviewRule {
    @Override
    public List<CheckoutViolation> validate(CheckoutContext context) {
    List<CheckoutViolation> inventoryViolate = new ArrayList<>();
    for(CheckoutItemSnapshot checkoutItemSnapshot : context.items()){
        InventoryEntity inventory = context.requireInventory(checkoutItemSnapshot.product().getId());


    }
        return List.of();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
