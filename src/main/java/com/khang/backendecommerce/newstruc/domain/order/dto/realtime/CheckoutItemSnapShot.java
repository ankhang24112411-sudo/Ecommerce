package com.khang.backendecommerce.newstruc.domain.order.dto.realtime;

import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CheckoutItemSnapShot(
    String cartItemId,
    String productId,
    String shopId,
    String sku,
    String productName,
    BigDecimal currentUnitPrice,
    int quantity
            )
{
//    public CheckoutItemSnapShot{
//        if(quantity <= 0){
//            throw ApplicationErrors.INVALID_QUANTITY;
//        }
//        if(currentUnitPrice.signum() <= 0){
//            throw ApplicationErrors.INVALID_PRODUCT_PRICE;
//        }
//    }
}
