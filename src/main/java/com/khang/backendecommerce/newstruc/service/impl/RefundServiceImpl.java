package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.infrastructure.common.enums.PaymentStatus;
import com.khang.backendecommerce.newstruc.entity.OrderEntity;
import com.khang.backendecommerce.newstruc.entity.PaymentEntity;
import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;
import com.khang.backendecommerce.newstruc.entity.UserEntity;
import com.khang.backendecommerce.newstruc.repo.PaymentRepository;
import com.khang.backendecommerce.newstruc.service.RefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "REFUND- SERVICE")
public class RefundServiceImpl implements RefundService {
    private final PaymentRepository paymentRepository;
    @Override
    public void handleRefundWhenSubOrderReject(SubOrderEntity subOrder, OrderEntity order, UserEntity user) {

        Optional<PaymentEntity> payment = paymentRepository.findByUser_IdAndOrder_Id(user.getId(),order.getId()).stream()
                .filter(paymentEntity -> paymentEntity.getPaymentStatus().equals(PaymentStatus.PAID))
                .findFirst();
        if(payment.isEmpty()){
            return;
        }
    }
}
