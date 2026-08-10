package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.infrastructure.common.enums.PaymentStatus;
import com.khang.backendecommerce.infrastructure.common.enums.RefundSource;
import com.khang.backendecommerce.infrastructure.common.enums.RefundStatus;
import com.khang.backendecommerce.newstruc.entity.*;
import com.khang.backendecommerce.newstruc.repo.PaymentRepository;
import com.khang.backendecommerce.newstruc.repo.RefundRepository;
import com.khang.backendecommerce.newstruc.service.RefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "REFUND- SERVICE")
public class RefundServiceImpl implements RefundService {
    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepo;
    @Override
    public void handleRefundWhenSubOrderReject(SubOrderEntity subOrder, OrderEntity order, UserEntity user, BigDecimal refundAmount) {

        Optional<PaymentEntity> payment = paymentRepository.findByUser_IdAndOrder_Id(user.getId(),order.getId()).stream()
                .filter(paymentEntity -> paymentEntity.getPaymentStatus().equals(PaymentStatus.PAID))
                .findFirst();
        if(payment.isEmpty()){
            return;
        }
        RefundEntity refundEntity = RefundEntity.builder()
                .payment(payment.get())
                .order(order)
                .subOrder(subOrder)
                .source(RefundSource.SUBORDER_REJECTED)
                .amount(refundAmount)
                .status(RefundStatus.PENDING)
                .reason("REJECTED FROM STORE")
                .build();
        refundRepo.save(refundEntity);
    }
}
