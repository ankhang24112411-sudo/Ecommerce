package com.khang.backendecommerce.newstruc.domain.suborder;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;
import org.springframework.stereotype.Component;

@Component
public class ConfirmedState implements SubOrderState {

    @Override
    public void startPicking(SubOrderEntity subOrder) {
        subOrder.setOrderStatus(OrderStatus.PICKING);
    }

    @Override
    public OrderStatus getCurrentState(SubOrderEntity subOrder) {
        return subOrder.getOrderStatus();
    }
}