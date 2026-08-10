package com.khang.backendecommerce.newstruc.domain.suborder;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;
import org.springframework.stereotype.Component;

@Component
public class PickingState implements SubOrderState {

    @Override
    public void startShipping(SubOrderEntity subOrder) {
        subOrder.setOrderStatus(OrderStatus.SHIPPING);
    }

    @Override
    public OrderStatus getCurrentState(SubOrderEntity subOrder) {
        return subOrder.getOrderStatus();
    }
}
