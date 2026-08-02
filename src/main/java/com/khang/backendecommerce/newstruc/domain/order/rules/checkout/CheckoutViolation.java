package com.khang.backendecommerce.newstruc.domain.order.rules.checkout;

import com.khang.backendecommerce.infrastructure.exception.ApplicationException;

import java.util.Objects;

public record CheckoutViolation(
        ApplicationException error,

        String field,
        String referenceId
) { public static CheckoutViolation of(ApplicationException error, String field, Object referenceId) {
        Objects.requireNonNull(error, "error must not be null");

        return new CheckoutViolation(
                error,
                field,
                referenceId == null ? null : referenceId.toString());
    }

}
