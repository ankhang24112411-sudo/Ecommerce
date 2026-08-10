package com.khang.backendecommerce.newstruc.domain.suborder;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;
import org.springframework.stereotype.Component;

@Component
public class PendingState implements SubOrderState {

    @Override
    public void nextState(SubOrderEntity subOrder) {
        subOrder.setOrderStatus(OrderStatus.CONFIRMED);
    }
    @Override
    public void previousState(SubOrderEntity subOrder) {
    }
    @Override
    public OrderStatus getCurrentState(SubOrderEntity subOrder) {
        return subOrder.getOrderStatus();
    }
}