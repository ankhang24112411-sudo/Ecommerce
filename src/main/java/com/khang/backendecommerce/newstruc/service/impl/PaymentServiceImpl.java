package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.newstruc.dto.request.MockPaymentWebhookRequest;
import com.khang.backendecommerce.newstruc.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    @Override
    public void mockWebhooks(MockPaymentWebhookRequest request) {

    }
}
