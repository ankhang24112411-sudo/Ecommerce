package com.khang.backendecommerce.newstruc.domain.order.checkout;

import com.khang.backendecommerce.infrastructure.common.enums.CheckoutSource;
import com.khang.backendecommerce.newstruc.domain.order.checkout.registry.CheckoutContext;
import com.khang.backendecommerce.newstruc.domain.order.dto.request.OrderCommand;

public interface CheckoutSourceStrategy {
    CheckoutSource source();
    CheckoutContext load (String userId , OrderCommand command);

    default void complete(CheckoutContext checkout) {

    }
}
