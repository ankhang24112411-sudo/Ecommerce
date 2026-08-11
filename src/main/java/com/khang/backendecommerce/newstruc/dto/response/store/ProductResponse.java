package com.khang.backendecommerce.newstruc.dto.response.store;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record ProductResponse(
        String id,
        String name,
        BigDecimal price
) {}