package com.khang.backendecommerce.newstruc.domain.suborder.config;

import com.khang.backendecommerce.newstruc.domain.suborder.SubOrderState;
import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubOrderStateService {
    private final SubOrderStateContext stateContext;
    @Transactional
    public void confirm(SubOrderEntity subOrder) {
        SubOrderState state = stateContext.getState(subOrder);
        state.confirm(subOrder);
    }

    @Transactional
    public void startPicking(SubOrderEntity subOrder) {
        SubOrderState state = stateContext.getState(subOrder);
        state.startPicking(subOrder);
    }

    @Transactional
    public void startShipping(SubOrderEntity subOrder) {
        SubOrderState state = stateContext.getState(subOrder);
        state.startShipping(subOrder);
    }
    @Transactional
    public void delivered(SubOrderEntity subOrder) {
        SubOrderState state = stateContext.getState(subOrder);
        state.delivered(subOrder);
    }
    @Transactional
    public void deliveryFailed(SubOrderEntity subOrder) {
        SubOrderState state = stateContext.getState(subOrder);
        state.failed(subOrder);
    }
    @Transactional
    public void returning(SubOrderEntity subOrder) {
        SubOrderState state = stateContext.getState(subOrder);
        state.returning(subOrder);
    }

    @Transactional
    public void reattempt(SubOrderEntity subOrder) {
        SubOrderState state = stateContext.getState(subOrder);
        state.reattempt(subOrder);
    }

    @Transactional
    public void reject(SubOrderEntity subOrder){
        SubOrderState state = stateContext.getState(subOrder);
        state.reject(subOrder);
    }

}