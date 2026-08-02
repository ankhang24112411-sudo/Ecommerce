package com.khang.backendecommerce.newstruc.domain.order.dto.realtime;

import com.khang.backendecommerce.infrastructure.common.enums.CheckoutSource;
import lombok.Builder;

import java.util.List;
import java.util.Objects;

@Builder
public record CheckoutSnapshot (
     String userId,
    CheckoutSource source,
    String cartId,
    List<CheckoutItemSnapShot> items ,
    List<String> productIds,
    String discountId
)
    {
    public CheckoutSnapshot{
        Objects.requireNonNull(source);
        productIds = List.copyOf(productIds);
        items = List.copyOf(items);


    }
}
