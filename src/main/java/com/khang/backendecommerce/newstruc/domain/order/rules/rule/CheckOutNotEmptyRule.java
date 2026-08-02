package com.khang.backendecommerce.newstruc.domain.order.rules.rule;

import com.khang.backendecommerce.newstruc.domain.order.rules.checkout.CheckoutContext;
import com.khang.backendecommerce.newstruc.domain.order.rules.checkout.CheckoutRule;
import com.khang.backendecommerce.newstruc.domain.order.rules.checkout.CheckoutViolation;

import java.util.List;

public class CheckOutNotEmptyRule implements CheckoutRule {
    @Override
    public List<CheckoutViolation> validate(CheckoutContext context) {
        if(!context.items().isEmpty())
        return List.of();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
