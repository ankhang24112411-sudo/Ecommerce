package com.khang.backendecommerce.newstruc.domain.suborder;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;
import org.springframework.stereotype.Component;

@Component
public class ShippingState implements SubOrderState {

    @Override
    public void delivered(SubOrderEntity subOrder) {
        subOrder.setOrderStatus(OrderStatus.DELIVERED);
    }

    @Override

    public void firstReattempt(SubOrderEntity subOrder) {
        subOrder.setOrderStatus(OrderStatus.REATTEMPT);
    }


    @Override
    public void failed(SubOrderEntity subOrder) {
        subOrder.setOrderStatus(OrderStatus.FAILED);
    }

    @Override
    public OrderStatus getCurrentState(SubOrderEntity subOrder) {
        return subOrder.getOrderStatus();
    }
}