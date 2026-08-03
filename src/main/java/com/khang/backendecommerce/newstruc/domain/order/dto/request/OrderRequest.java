package com.khang.backendecommerce.newstruc.domain.order.dto.request;

import com.khang.backendecommerce.infrastructure.common.enums.CheckoutSource;
import com.khang.backendecommerce.infrastructure.common.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter

public class OrderRequest {

    @NotNull
    private PaymentMethod paymentMethod;
    @Size(max = 500)
    private String userNotes;
}
