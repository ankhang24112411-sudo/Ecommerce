package com.khang.backendecommerce.newstruc.domain.order.dto.request;

import com.khang.backendecommerce.infrastructure.common.enums.CheckoutSource;
import com.khang.backendecommerce.infrastructure.common.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderCommand {
    private String userId;
    private  String stateId;
    private String address;
    private String discountCusId;
    @NotNull
    private CheckoutSource checkoutSource;
    @NotNull
    private PaymentMethod paymentMethod;
    @Size(max = 500)
    private String userNotes;
}
