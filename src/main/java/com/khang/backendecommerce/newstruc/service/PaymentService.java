package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.newstruc.dto.request.MockPaymentWebhookRequest;
import com.khang.backendecommerce.newstruc.dto.request.MockRefundWebhookRequest;

public interface PaymentService {
    String mockWebhooks(MockPaymentWebhookRequest request);

    String mockingWebhooksRefundPayment(MockRefundWebhookRequest request);
}
