package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.newstruc.dto.response.TrackingSubOrderResponse;
import com.khang.backendecommerce.newstruc.entity.OrderEntity;
import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;
import com.khang.backendecommerce.newstruc.entity.UserEntity;

public interface TrackingService {
    void createTrackingAndTrackingLog(UserEntity user, SubOrderEntity subOrder, OrderEntity order);

    TrackingSubOrderResponse picking(String trackingCode);

    TrackingSubOrderResponse shipping(String trackingCode);
}
