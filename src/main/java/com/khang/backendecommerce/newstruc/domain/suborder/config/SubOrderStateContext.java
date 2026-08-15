package com.khang.backendecommerce.newstruc.domain.suborder.config;

import com.khang.backendecommerce.newstruc.domain.suborder.*;
import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubOrderStateContext {
    private final PendingState pendingState;
    private final ConfirmedState confirmedState;
    private final PickingState pickingState;
    private final ShippingState shippingState;
    private final DeliveredState deliveredState;
    private final FailedState failedState;
    private final ReturningState returningState;
    private final ReattemptState reattemptState;

    public SubOrderState getState(SubOrderEntity subOrder) {
        return switch (subOrder.getOrderStatus()) {
            case PENDING -> pendingState;
            case CONFIRMED -> confirmedState;
            case PICKING -> pickingState;
            case SHIPPING -> shippingState;
            case DELIVERED -> deliveredState;
            case FAILED -> failedState;
            case RETURNING -> returningState;
            case REATTEMPT -> reattemptState;
        };
    }
}