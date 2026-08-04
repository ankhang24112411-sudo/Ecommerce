package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.newstruc.dto.request.MockPaymentWebhookRequest;
import com.khang.backendecommerce.newstruc.entity.PaymentEntity;
import com.khang.backendecommerce.newstruc.repo.PaymentRepository;
import com.khang.backendecommerce.newstruc.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepo;
    @Override
    public void mockWebhooks(MockPaymentWebhookRequest request) {
        PaymentEntity payment = paymentRepo.findByPaymentReferenceAndUser_Id(request.paymentReference(), request.userId())
                .orElseThrow(() -> ApplicationErrors.PAYMENT_REFERENCE_CODE_NOT_FOUND);
    }
}
