package com.khang.backendecommerce.newstruc.dto.request;

import com.khang.backendecommerce.infrastructure.common.enums.PaymentStatus;

public record MockRefundWebhookRequest(
        String refundId,
        String providerReference,
        PaymentStatus status
) {
}
