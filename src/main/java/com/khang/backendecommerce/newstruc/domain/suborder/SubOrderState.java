package com.khang.backendecommerce.newstruc.domain.suborder;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;

public interface SubOrderState {
    void nextState(SubOrderEntity subOrder);

    void previousState(SubOrderEntity subOrder);

    OrderStatus getCurrentState(SubOrderEntity subOrder);
}
