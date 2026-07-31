package com.khang.backendecommerce.infrastructure.common.dto.response;

public record FieldViolation(
        String field,
        String message) {

}
