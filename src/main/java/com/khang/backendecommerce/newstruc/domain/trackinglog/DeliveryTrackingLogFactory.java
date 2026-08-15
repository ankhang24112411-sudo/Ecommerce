package com.khang.backendecommerce.newstruc.domain.trackinglog;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.newstruc.entity.DeliveryEntity;
import com.khang.backendecommerce.newstruc.entity.DeliveryTrackingLog;
import com.khang.backendecommerce.newstruc.entity.OrderEntity;
import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;
import org.springframework.stereotype.Component;

@Component
public class DeliveryTrackingLogFactory {

    public DeliveryTrackingLog create(DeliveryEntity delivery, SubOrderEntity subOrder, String location) {

        OrderEntity order = subOrder.getOrder();

        return DeliveryTrackingLog.builder()
                .delivery(delivery)
                .trackingCode(subOrder.getTrackingCode())
                .message(resolveMessage(subOrder.getOrderStatus()))
                .location(location)
                .status(subOrder.getOrderStatus())
                .subOrder(subOrder)
                .order(order)
                .receiverName(order.getCustomerName())
                .receiverAddress(order.getAddress())
                .receiverPhone(order.getCustomer().getPhone()).build();
    }

    private String resolveMessage(OrderStatus status) {
        return switch (status) {
            case CONFIRMED -> "Order confirmed";
            case PICKING -> "Warehouse is preparing your order";
            case SHIPPING -> "Order is being delivered";
            case DELIVERED -> "Order delivered successfully";
            case FAILED -> "Delivery failed";
            case RETURNING -> "Order is being returned";
            case REATTEMPT -> "Order will be delivered again";
            default -> "Order status updated";
        };
    }

}

