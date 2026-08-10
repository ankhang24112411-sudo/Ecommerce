package com.khang.backendecommerce.newstruc.domain.suborder;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;
import org.springframework.stereotype.Component;

@Component
public class PendingState implements SubOrderState {

    @Override
    public void confirm(SubOrderEntity subOrder) {
        subOrder.setOrderStatus(OrderStatus.CONFIRMED);
    }

    @Override
    public OrderStatus getCurrentState(SubOrderEntity subOrder) {
        return subOrder.getOrderStatus();
    }
    @Override
    public void reject(SubOrderEntity subOrder){
        subOrder.setOrderStatus(OrderStatus.FAILED);
    }
}
