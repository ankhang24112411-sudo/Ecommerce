package com.khang.backendecommerce.newstruc.domain.suborder;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;
import org.springframework.stereotype.Component;

@Component
public class ReattemptState implements SubOrderState {
    @Override
    public void reattempt(SubOrderEntity subOrder){
        if(subOrder.getAttemptCount() >= 3){
            subOrder.setOrderStatus(OrderStatus.FAILED);
        }
        subOrder.setOrderStatus(OrderStatus.REATTEMPT);
    }

    @Override
    public OrderStatus getCurrentState(SubOrderEntity subOrder) {
        return subOrder.getOrderStatus();
    }
}