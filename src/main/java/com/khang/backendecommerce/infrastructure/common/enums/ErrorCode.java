package com.khang.backendecommerce.infrastructure.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    CART_ITEM_NOT_FOUND("CART_ITEM_NOT_FOUND", "Cannot find your cart item", HttpStatus.NOT_FOUND),
    INVALID_QUANTITY("INVALID_QUANTITY", "Quantity is not valid", HttpStatus.BAD_REQUEST),

    INSUFFICIENT_STOCK("INSUFFICIENT_STOCK", "Product stock is insufficient", HttpStatus.CONFLICT),
    DISCOUNT_EXPIRED("DISCOUNT_EXPIRED", "Discount has expired", HttpStatus.CONFLICT),

    DISCOUNT_NOT_APPLICABLE("DISCOUNT_NOT_APPLICABLE", "Discount is not applicable", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
