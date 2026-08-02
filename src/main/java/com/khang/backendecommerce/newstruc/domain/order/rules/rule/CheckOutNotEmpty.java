package com.khang.backendecommerce.newstruc.domain.order.rules.rule;

import com.khang.backendecommerce.newstruc.domain.order.dto.realtime.CheckoutContext;
import com.khang.backendecommerce.newstruc.domain.order.rules.CheckoutRule;
import com.khang.backendecommerce.newstruc.domain.order.rules.config.CheckoutViolation;

import java.util.List;

public class CheckOutNotEmpty implements CheckoutRule {
    @Override
    public List<CheckoutViolation> validate(CheckoutContext context) {
        return List.of();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
