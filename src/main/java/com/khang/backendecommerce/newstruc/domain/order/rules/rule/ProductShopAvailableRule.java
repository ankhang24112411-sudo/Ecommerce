package com.khang.backendecommerce.newstruc.domain.order.rules.rule;

import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.newstruc.domain.order.checkout.registry.CheckoutContext;
import com.khang.backendecommerce.newstruc.domain.order.checkout.registry.CheckoutItemSnapshot;
import com.khang.backendecommerce.newstruc.domain.order.rules.checkout.CheckoutRule;
import com.khang.backendecommerce.newstruc.domain.order.rules.checkout.CheckoutViolation;
import com.khang.backendecommerce.newstruc.domain.order.rules.checkout.task.CheckoutPreviewRule;

import java.util.ArrayList;
import java.util.List;

public class ProductShopAvailableRule implements CheckoutRule, CheckoutPreviewRule {
    @Override
    public List<CheckoutViolation> validate(CheckoutContext context) {
        List<CheckoutViolation> productInactive = context.items().stream()
              .filter(item -> item.product().getDeleted() == 1)
              .map(item -> CheckoutViolation.of(ApplicationErrors.PRODUCT_INACTIVE, "Product is not activated", item.product().getId()))
              .toList();
        Lis
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
