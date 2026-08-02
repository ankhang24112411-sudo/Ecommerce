package com.khang.backendecommerce.newstruc.domain.order.rules.rule;

import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.infrastructure.exception.ApplicationException;
import com.khang.backendecommerce.newstruc.domain.order.rules.checkout.CheckoutContext;
import com.khang.backendecommerce.newstruc.domain.order.rules.checkout.CheckoutRule;
import com.khang.backendecommerce.newstruc.domain.order.rules.checkout.CheckoutViolation;

import java.util.List;

public class CheckOutNotEmptyRule implements CheckoutRule {
    @Override
    public List<CheckoutViolation> validate(CheckoutContext context) {
        if(!context.items().isEmpty()) {
            return List.of();
        }
        return List.of(CheckoutViolation.of(ApplicationErrors.CART_ITEM_NOT_FOUND, "items" , null));
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
