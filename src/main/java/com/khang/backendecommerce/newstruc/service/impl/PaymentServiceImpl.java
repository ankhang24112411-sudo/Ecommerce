package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.infrastructure.common.enums.PaymentStatus;
import com.khang.backendecommerce.infrastructure.common.enums.RefundStatus;
import com.khang.backendecommerce.infrastructure.configuration.CurrentUserProvider;
import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.infrastructure.util.AppConst;
import com.khang.backendecommerce.newstruc.domain.order.service.OrderService;
import com.khang.backendecommerce.newstruc.dto.request.MockPaymentWebhookRequest;
import com.khang.backendecommerce.newstruc.dto.request.MockRefundWebhookRequest;
import com.khang.backendecommerce.newstruc.entity.OrderEntity;
import com.khang.backendecommerce.newstruc.entity.PaymentEntity;
import com.khang.backendecommerce.newstruc.entity.RefundEntity;
import com.khang.backendecommerce.newstruc.entity.UserEntity;
import com.khang.backendecommerce.newstruc.repo.OrderRepository;
import com.khang.backendecommerce.newstruc.repo.PaymentRepository;
import com.khang.backendecommerce.newstruc.repo.RefundRepository;
import com.khang.backendecommerce.newstruc.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepo;
    private final CurrentUserProvider currentUserProvider;
    private final OrderRepository orderRepo;
    private final OrderService orderService;
    private final RefundRepository refundRepos;

    @Override
    public String mockWebhooks(MockPaymentWebhookRequest request) {
        String paymentReference = null;
        UserEntity user = currentUserProvider.getCurrentUser();
        PaymentEntity payment = paymentRepo.findByPaymentReferenceAndUser_Id(request.paymentReference(), request.userId())
                .orElseThrow(() -> ApplicationErrors.PAYMENT_REFERENCE_CODE_NOT_FOUND);
//        if(payment.)
        OrderEntity order = payment.getOrder();
        switch (request.paymentStatus()) {
            case FAILED -> {
                order.setPaymentStatus(PaymentStatus.AWAITING_PAYMENT);
                payment.setPaymentStatus(PaymentStatus.FAILED);
                payment.setFailedAt(Instant.now());
                paymentReference = AppConst.paymentReference;
                PaymentEntity newPayment = PaymentEntity.builder()
                        .user(user)
                        .order(order)
                        .paymentReference(paymentReference)
                        .paymentStatus(PaymentStatus.AWAITING_PAYMENT)
                        .build();
                paymentRepo.save(newPayment);
            }
            case PAID -> {

                order.setPaymentStatus(PaymentStatus.PAID);
                order.setOrderStatus(OrderStatus.PENDING);
                payment.setPaymentStatus(PaymentStatus.PAID);
                orderService.deleteCartAndUpdateInventoryAfterPaymentSuccess(user, order, payment);
            }
        }
        orderRepo.save(order);
        paymentRepo.save(payment);
        if (paymentReference != null) {
            return "Payment failed , new PaymentReference : " + paymentReference;
        }
        return "Payment progress success";
    }

    @Override
    public String mockingWebhooksRefundPayment(MockRefundWebhookRequest request) {
        RefundEntity refund = refundRepos.findById(request.refundId()).orElseThrow(() -> ApplicationErrors.REFUND_NOT_FOUND);
        refund.setProviderReference("MOCK-PROVIDER");
        refund.setStatus(RefundStatus.SUCCESS);
        return "";
    }
}
