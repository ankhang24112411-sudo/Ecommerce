package com.khang.backendecommerce.newstruc.domain.order.rules;

import com.khang.backendecommerce.newstruc.domain.order.dto.realtime.CheckoutContext;
import com.khang.backendecommerce.newstruc.domain.order.rules.config.CheckoutViolation;

public interface CheckoutRule extends BusinessRule<CheckoutContext, CheckoutViolation> {
}
