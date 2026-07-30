package com.khang.backendecommerce.domain.cart.projection;

import com.khang.backendecommerce.domain.cart.dto.request.CartItemPriceResponse;
import com.khang.backendecommerce.domain.cart.entity.CartItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface CartMapper {
    CartItemPriceResponse toCartItemPriceResponse(CartItemEntity cartItem);
}
