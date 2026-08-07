package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.newstruc.dto.request.MockPaymentWebhookRequest;

public interface PaymentService {
    String mockWebhooks(MockPaymentWebhookRequest request);
}
