package com.khang.backendecommerce.newstruc.domain.order.rules.checkout;


import lombok.Getter;

import java.util.List;

@Getter
public class CheckoutValidationException extends RuntimeException {
    private final List<CheckoutViolation> violations;

    public CheckoutValidationException(List<CheckoutViolation> violations) {
        super("Checkout validation failed");
        this.violations = List.copyOf(violations);
    }
}