package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.newstruc.domain.order.OrderResultCalculation;
import com.khang.backendecommerce.newstruc.domain.suborder.config.SubOrderStateService;
import com.khang.backendecommerce.newstruc.domain.trackinglog.DeliveryTrackingLogFactory;
import com.khang.backendecommerce.newstruc.dto.response.TrackingSubOrderResponse;
import com.khang.backendecommerce.newstruc.entity.*;
import com.khang.backendecommerce.newstruc.repo.SubOrderRepository;
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
    private final SubOrderRepository subOrderRepo;
    private final DeliveryTrackingLogFactory deliveryTrackingLogFactory;
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

        DeliveryTrackingLog deliveryTrackingLog = deliveryTrackingLogFactory.create(delivery,subOrder,"STORE");
        trackingRepository.save(delivery);
        trackingLogRepository.save(deliveryTrackingLog);
     }

    @Override
    public TrackingSubOrderResponse picking(String trackingCode) {
        SubOrderEntity subOrder = subOrderRepo.findByTrackingCode(trackingCode);
        OrderEntity order = subOrder.getOrder();
        DeliveryEntity delivery = trackingRepository.findBySubOrder_Id(subOrder.getId());
        subOrderStateService.startPicking(subOrder);
        DeliveryTrackingLog deliveryTrackingLog = deliveryTrackingLogFactory.create(delivery,subOrder,"WAREHOUSE");

        trackingRepository.save(delivery);
        trackingLogRepository.save(deliveryTrackingLog);
        return TrackingSubOrderResponse.builder()
                .orderCode(order.getOrderCode()).
                trackingCode(subOrder.getTrackingCode())
                .message(deliveryTrackingLog.getMessage())
                .updatedAt(deliveryTrackingLog.getCreatedAt())
                .status(subOrder.getOrderStatus())
                .location("STORE")
                .build();
    }

    @Override
    public TrackingSubOrderResponse shipping(String trackingCode) {
        SubOrderEntity subOrder = subOrderRepo.findByTrackingCode(trackingCode);
        OrderEntity order = subOrder.getOrder();
        DeliveryEntity delivery = trackingRepository.findBySubOrder_Id(subOrder.getId());
        subOrderStateService.startShipping(subOrder);
        DeliveryTrackingLog deliveryTrackingLog = deliveryTrackingLogFactory.create(delivery,subOrder,"WAREHOUSE");

        trackingRepository.save(delivery);
        trackingLogRepository.save(deliveryTrackingLog);
        return TrackingSubOrderResponse.builder()
                .orderCode(order.getOrderCode()).
                trackingCode(subOrder.getTrackingCode())
                .message(deliveryTrackingLog.getMessage())
                .updatedAt(deliveryTrackingLog.getCreatedAt())
                .status(subOrder.getOrderStatus())
                .location(deliveryTrackingLog.getLocation())
                .build();
    }

    @Override
    public TrackingSubOrderResponse completed(String trackingCode) {
        SubOrderEntity subOrder = subOrderRepo.findByTrackingCode(trackingCode);
        OrderEntity order = subOrder.getOrder();
        DeliveryEntity delivery = trackingRepository.findBySubOrder_Id(subOrder.getId());
        subOrderStateService.delivered(subOrder);
        DeliveryTrackingLog deliveryTrackingLog = deliveryTrackingLogFactory.create(delivery,subOrder,delivery.getReceiverAddress());

        trackingRepository.save(delivery);
        trackingLogRepository.save(deliveryTrackingLog);
        return TrackingSubOrderResponse.builder()
                .orderCode(order.getOrderCode()).
                trackingCode(subOrder.getTrackingCode())
                .message(deliveryTrackingLog.getMessage())
                .updatedAt(deliveryTrackingLog.getCreatedAt())
                .status(subOrder.getOrderStatus())
                .location(deliveryTrackingLog.getLocation())
                .build();

    }
}
