package com.khang.backendecommerce.newstruc.dto.request;

import com.khang.backendecommerce.infrastructure.common.enums.PaymentStatus;

import java.math.BigDecimal;

public record MockPaymentWebhookRequest (
        String userId,
        String orderId,
        String paymentReference,
        PaymentStatus paymentStatus
){
}
