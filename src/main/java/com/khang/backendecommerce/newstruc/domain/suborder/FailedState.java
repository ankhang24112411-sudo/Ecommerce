package com.khang.backendecommerce.newstruc.domain.suborder;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;
import org.springframework.stereotype.Component;

@Component
public class FailedState implements SubOrderState {

    @Override
    public void returning(SubOrderEntity subOrder) {
        subOrder.setOrderStatus(OrderStatus.RETURNING);
    }

    @Override
    public void reattempt(SubOrderEntity subOrder) {
        subOrder.setOrderStatus(OrderStatus.REATTEMPT);
    }

    @Override
    public OrderStatus getCurrentState(SubOrderEntity subOrder) {
        return subOrder.getOrderStatus();
    }
}
