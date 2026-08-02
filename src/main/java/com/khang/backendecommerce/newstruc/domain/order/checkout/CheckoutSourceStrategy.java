package com.khang.backendecommerce.newstruc.domain.order.checkout;

import com.khang.backendecommerce.infrastructure.common.enums.CheckoutSource;
import com.khang.backendecommerce.newstruc.domain.order.dto.OrderCommand;
import com.khang.backendecommerce.newstruc.domain.order.dto.realtime.CheckoutSnapshot;

public interface CheckoutSourceStrategy {
    CheckoutSource source();
    CheckoutSnapshot load (String userId , OrderCommand command);

    default void complete(CheckoutSnapshot checkout) {

    }
}
