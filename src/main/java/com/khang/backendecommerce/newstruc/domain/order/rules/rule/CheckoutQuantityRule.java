package com.khang.backendecommerce.newstruc.domain.order.rules.rule;

import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.newstruc.domain.order.checkout.registry.CheckoutContext;
import com.khang.backendecommerce.newstruc.domain.order.rules.checkout.CheckoutRule;
import com.khang.backendecommerce.newstruc.domain.order.rules.checkout.CheckoutViolation;

import java.util.List;

public class CheckoutQuantityRule implements CheckoutRule {


    @Override
    public List<CheckoutViolation> validate(CheckoutContext context) {

        return context.items().stream()
                .filter(item -> item.quantity() <= 0)
                .map(item -> CheckoutViolation.of(ApplicationErrors.INVALID_QUANTITY, "Quantity is null ", item.product().getId()))
                .toList();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
