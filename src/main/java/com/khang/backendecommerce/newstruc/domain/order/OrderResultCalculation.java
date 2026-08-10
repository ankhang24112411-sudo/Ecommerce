package com.khang.backendecommerce.newstruc.domain.order;

import com.khang.backendecommerce.infrastructure.common.enums.OrderResult;
import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;
import org.springframework.stereotype.Component;

import java.util.List;
@Component

public class OrderResultCalculation{

    public OrderResult calculate(List<SubOrderEntity> subOrders) {
        if (subOrders == null || subOrders.isEmpty()) {
            return OrderResult.PENDING;
        }
        boolean allDelivered = subOrders.stream().allMatch(subOrder->subOrder.getOrderStatus()== OrderStatus.DELIVERED);
        if (allDelivered) {
            return OrderResult.SUCCESS;
        }
        boolean allFailed = subOrders.stream().allMatch(subOrder-> subOrder.getOrderStatus() ==OrderStatus.FAILED);
        if (allFailed) {
            return OrderResult.FAILED;
        }
        boolean hasDelivered= subOrders.stream().anyMatch(subOrder -> subOrder.getOrderStatus() == OrderStatus.DELIVERED);
        boolean hasFailed = subOrders.stream().anyMatch(subOrder -> subOrder.getOrderStatus() == OrderStatus.FAILED);
        if (hasDelivered && hasFailed) {
            return OrderResult.PARTIAL_SUCCESS;
        }
        return OrderResult.PENDING;
    }
}