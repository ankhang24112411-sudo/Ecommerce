package com.khang.backendecommerce.newstruc.projection;

import com.khang.backendecommerce.newstruc.dto.request.CartItemPriceResponse;
import com.khang.backendecommerce.newstruc.entity.CartItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface CartMapper {

    CartItemPriceResponse toCartItemPriceResponse(CartItemEntity cartItem);
}
