package com.khang.backendecommerce.newstruc.domain.suborder;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;

public interface SubOrderState {
    default void confirm(SubOrderEntity subOrder) {
        throw invalidActioning(subOrder, "CONFIRM");
    }
    default void startPicking(SubOrderEntity subOrder) {
        throw invalidActioning(subOrder, "START_PICKING");
    }

    default void startShipping(SubOrderEntity subOrder) {
        throw invalidActioning(subOrder, "START_SHIPPING");
    }

    default void delivered(SubOrderEntity subOrder) {
        throw invalidActioning(subOrder, "DELIVERED");
    }

    default void failed(SubOrderEntity subOrder) {
        throw invalidActioning(subOrder, "FAILED");
    }

    default void returning(SubOrderEntity subOrder) {
        throw invalidActioning(subOrder, "RETURNING");
    }

    default void reattempt(SubOrderEntity subOrder) {
        throw invalidActioning(subOrder, "REATTEMPT");
    }
    default void reject(SubOrderEntity subOrder){

    }
    OrderStatus getCurrentState(SubOrderEntity subOrder);

    private RuntimeException invalidActioning(SubOrderEntity subOrder, String action) {
        return new IllegalStateException("Action " + action + " is not allowed when SubOrder status is " + subOrder.getOrderStatus());
    }
}


