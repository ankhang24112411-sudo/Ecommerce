package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.newstruc.domain.order.OrderResultCalculation;
import com.khang.backendecommerce.newstruc.domain.suborder.config.SubOrderStateService;
import com.khang.backendecommerce.newstruc.entity.*;
import com.khang.backendecommerce.newstruc.repo.TrackingLogRepository;
import com.khang.backendecommerce.newstruc.repo.TrackingRepository;
import com.khang.backendecommerce.newstruc.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TrackingServiceImpl implements TrackingService {
    private final TrackingRepository trackingRepository;
    private final TrackingLogRepository trackingLogRepository;
    private final OrderResultCalculation orderResultCalculation;
    private final SubOrderStateService subOrderStateService;
    private static final DateTimeFormatter ORDER_CODE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    private String generateSubTrackingCode() {
        String date = LocalDate.now().format(ORDER_CODE_FORMAT);
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "SUBORD-" + date + "-" + randomPart;
}
    @Override
    public void createTrackingAndTrackingLog(UserEntity user, SubOrderEntity subOrder, OrderEntity order) {
     String trackingCode = generateSubTrackingCode();
     subOrder.setTrackingCode(trackingCode);
        DeliveryEntity delivery = DeliveryEntity.builder()
                .order(order)
                .subOrder(subOrder)
                .customer(order.getCustomer())
                .trackingCode(trackingCode)
                .receiverName(order.getCustomerName())
                .receiverAddress(order.getAddress()).build();

        DeliveryTrackingLog deliveryTrackingLog = DeliveryTrackingLog.builder()
                .delivery(delivery)
                .trackingCode(trackingCode)
                .message("Payment confirmed. Order is being prepared at warehouse")
                .location("WAREHOUSE")
                .status(order.getOrderStatus())
                .receiverName(order.getCustomerName())
                .receiverAddress(order.getAddress())
                .receiverPhone(user.getPhone())
                .subOrder(subOrder)
                .order(order)
                .build();
        trackingRepository.save(delivery);
        trackingLogRepository.save(deliveryTrackingLog);
     }
}
