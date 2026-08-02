package com.khang.backendecommerce.newstruc.domain.order.rules.checkout;

import com.khang.backendecommerce.newstruc.domain.order.checkout.registry.CheckoutContext;
import com.khang.backendecommerce.newstruc.domain.order.rules.rule.BusinessRule;

public interface CheckoutRule extends BusinessRule<CheckoutContext, CheckoutViolation> {
}
