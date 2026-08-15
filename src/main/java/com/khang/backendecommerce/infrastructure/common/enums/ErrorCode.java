package com.khang.backendecommerce.infrastructure.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    UTHENTICATION_REQUIRED("AUTHENTICATION_REQUIRED", "Authentication is required", HttpStatus.UNAUTHORIZED),

    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "Username or password is incorrect", HttpStatus.UNAUTHORIZED),

    ACCESS_DENIED("ACCESS_DENIED", "You do not have permission to perform this action", HttpStatus.FORBIDDEN),

    USER_NOT_FOUND("USER_NOT_FOUND", "User cannot be found", HttpStatus.NOT_FOUND),

    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", "User already exists", HttpStatus.CONFLICT),

    EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS", "Email address is already in use", HttpStatus.CONFLICT),

    USER_ACCOUNT_DISABLED("USER_ACCOUNT_DISABLED", "User account is disabled", HttpStatus.FORBIDDEN),

    INVALID_USER_STATUS("INVALID_USER_STATUS", "User status is not valid for this operation", HttpStatus.CONFLICT),

    ADDRESS_NOT_FOUND("ADDRESS_NOT_FOUND", "Delivery address cannot be found", HttpStatus.NOT_FOUND),

    ADDRESS_NOT_BELONG_TO_USER("ADDRESS_NOT_BELONG_TO_USER", "Delivery address does not belong to the current user", HttpStatus.FORBIDDEN),


    CART_NOT_FOUND("CART_NOT_FOUND", "Cart cannot be found", HttpStatus.NOT_FOUND),

    CART_ITEM_NOT_FOUND("CART_ITEM_NOT_FOUND", "Cannot find your cart item", HttpStatus.NOT_FOUND),

    CART_IS_EMPTY("CART_IS_EMPTY", "Cart is empty", HttpStatus.BAD_REQUEST),

    CART_ITEM_ALREADY_EXISTS("CART_ITEM_ALREADY_EXISTS", "Product already exists in the cart", HttpStatus.CONFLICT),


    INVALID_CART_ITEM("INVALID_CART_ITEM", "Cart item is not valid", HttpStatus.BAD_REQUEST),

    INVALID_QUANTITY("INVALID_QUANTITY", "Quantity is not valid", HttpStatus.BAD_REQUEST),

    MAX_QUANTITY_EXCEEDED("MAX_QUANTITY_EXCEEDED", "Maximum quantity per item has been exceeded", HttpStatus.BAD_REQUEST),

    // ==================== PRODUCT / INVENTORY ====================

    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", "Product cannot be found", HttpStatus.NOT_FOUND),

    PRODUCT_NOT_AVAILABLE("PRODUCT_NOT_AVAILABLE", "Product is currently unavailable", HttpStatus.CONFLICT),

    PRODUCT_INACTIVE("PRODUCT_INACTIVE", "Product is no longer active", HttpStatus.CONFLICT),

    PRODUCT_ALREADY_EXISTS("PRODUCT_ALREADY_EXISTS", "Product already exists", HttpStatus.CONFLICT),

    SKU_NOT_FOUND(
            "SKU_NOT_FOUND",
            "Product SKU cannot be found",
            HttpStatus.NOT_FOUND
    ),

    SKU_NOT_AVAILABLE(
            "SKU_NOT_AVAILABLE",
            "Product SKU is currently unavailable",
            HttpStatus.CONFLICT
    ),

    INVENTORY_NOT_FOUND(
            "INVENTORY_NOT_FOUND",
            "Inventory information cannot be found",
            HttpStatus.NOT_FOUND
    ),

    INSUFFICIENT_STOCK(
            "INSUFFICIENT_STOCK",
            "Product stock is insufficient",
            HttpStatus.CONFLICT
    ),

    PRODUCT_OUT_OF_STOCK(
            "PRODUCT_OUT_OF_STOCK",
            "Product is out of stock",
            HttpStatus.CONFLICT
    ),

    INVENTORY_UPDATE_CONFLICT(
            "INVENTORY_UPDATE_CONFLICT",
            "Inventory has changed, please try again",
            HttpStatus.CONFLICT
    ),

    // ==================== DISCOUNT / VOUCHER ====================

    DISCOUNT_NOT_FOUND(
            "DISCOUNT_NOT_FOUND",
            "Discount cannot be found",
            HttpStatus.NOT_FOUND
    ),

    INVALID_VOUCHER_CODE(
            "INVALID_VOUCHER_CODE",
            "Voucher code is invalid",
            HttpStatus.BAD_REQUEST
    ),

    DISCOUNT_EXPIRED(
            "DISCOUNT_EXPIRED",
            "Discount has expired",
            HttpStatus.CONFLICT
    ),

    DISCOUNT_NOT_STARTED(
            "DISCOUNT_NOT_STARTED",
            "Discount is not active yet",
            HttpStatus.CONFLICT
    ),

    DISCOUNT_INACTIVE(
            "DISCOUNT_INACTIVE",
            "Discount is currently inactive",
            HttpStatus.CONFLICT
    ),

    DISCOUNT_ALREADY_USED(
            "DISCOUNT_ALREADY_USED",
            "Discount has already been used",
            HttpStatus.CONFLICT
    ),


    DISCOUNT_EXCEEDS_SUBTOTAL(
            "DISCOUNT_EXCEEDS_SUBTOTAL",
            "Discount amount cannot exceed the order subtotal",
            HttpStatus.CONFLICT
    ),

    // ==================== CHECKOUT / ORDER ====================

    CHECKOUT_ITEM_NOT_FOUND(
            "CHECKOUT_ITEM_NOT_FOUND",
            "Checkout item cannot be found",
            HttpStatus.NOT_FOUND
    ),

    CHECKOUT_DATA_CHANGED(
            "CHECKOUT_DATA_CHANGED",
            "Checkout information has changed, please review the order again",
            HttpStatus.CONFLICT
    ),

    ORDER_NOT_FOUND(
            "ORDER_NOT_FOUND",
            "Order cannot be found",
            HttpStatus.NOT_FOUND
    ),

    ORDER_ITEM_NOT_FOUND(
            "ORDER_ITEM_NOT_FOUND",
            "Order item cannot be found",
            HttpStatus.NOT_FOUND
    ),

    ORDER_ALREADY_CREATED(
            "ORDER_ALREADY_CREATED",
            "Order has already been created",
            HttpStatus.CONFLICT
    ),

    INVALID_ORDER_STATUS(
            "INVALID_ORDER_STATUS",
            "Order status does not allow this operation",
            HttpStatus.CONFLICT
    ),

    ORDER_CANNOT_BE_CANCELLED(
            "ORDER_CANNOT_BE_CANCELLED",
            "Order can no longer be cancelled",
            HttpStatus.CONFLICT
    ),

    ORDER_ALREADY_CANCELLED(
            "ORDER_ALREADY_CANCELLED",
            "Order has already been cancelled",
            HttpStatus.CONFLICT
    ),

    ORDER_TOTAL_MISMATCH(
            "ORDER_TOTAL_MISMATCH",
            "Order total does not match the calculated amount",
            HttpStatus.CONFLICT
    ),

    // ==================== PAYMENT ====================

    PAYMENT_METHOD_NOT_SUPPORTED(
            "PAYMENT_METHOD_NOT_SUPPORTED",
            "Payment method is not supported",
            HttpStatus.BAD_REQUEST
    ),

    PAYMENT_NOT_FOUND(
            "PAYMENT_NOT_FOUND",
            "Payment information cannot be found",
            HttpStatus.NOT_FOUND
    ),

    PAYMENT_FAILED(
            "PAYMENT_FAILED",
            "Payment could not be completed",
            HttpStatus.CONFLICT
    ),

    PAYMENT_ALREADY_COMPLETED(
            "PAYMENT_ALREADY_COMPLETED",
            "Payment has already been completed",
            HttpStatus.CONFLICT
    ),

    PAYMENT_AMOUNT_MISMATCH(
            "PAYMENT_AMOUNT_MISMATCH",
            "Payment amount does not match the order total",
            HttpStatus.CONFLICT
    ),

    DELIVERY_ROUTE_NOT_FOUND(
            "DELIVERY_ROUTE_NOT_FOUND",
            "No delivery route is available",
            HttpStatus.NOT_FOUND
    ),

    DELIVERY_NOT_AVAILABLE(
            "DELIVERY_NOT_AVAILABLE",
            "Delivery is not available for this address",
            HttpStatus.CONFLICT
    ),

    DELIVERY_FEE_NOT_FOUND(
            "DELIVERY_FEE_NOT_FOUND",
            "Delivery fee cannot be determined",
            HttpStatus.NOT_FOUND
    ),

    STORE_NOT_FOUND(
            "STORE_NOT_FOUND",
            "Store cannot be found",
            HttpStatus.NOT_FOUND
    ),

    STORE_INACTIVE(
            "STORE_INACTIVE",
            "Store is currently inactive",
            HttpStatus.CONFLICT
    ),

    RETURN_REQUEST_NOT_FOUND(
            "RETURN_REQUEST_NOT_FOUND",
            "Return request cannot be found",
            HttpStatus.NOT_FOUND
    ),

    RETURN_PERIOD_EXPIRED(
            "RETURN_PERIOD_EXPIRED",
            "The return period has expired",
            HttpStatus.CONFLICT
    ),

    RETURN_NOT_ALLOWED(
            "RETURN_NOT_ALLOWED",
            "This order item is not eligible for return",
            HttpStatus.CONFLICT
    ),

    INVALID_REQUEST(
            "INVALID_REQUEST",
            "Request data is not valid",
            HttpStatus.BAD_REQUEST
    );
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
