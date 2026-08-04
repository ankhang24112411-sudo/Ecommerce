package com.khang.backendecommerce.newstruc.dto.request;

import com.khang.backendecommerce.infrastructure.common.enums.PaymentStatus;

import java.math.BigDecimal;

public record MockPaymentWebhookRequest (
        String orderId,
        String transactionId,
        BigDecimal amount,
        PaymentStatus status
){
}
