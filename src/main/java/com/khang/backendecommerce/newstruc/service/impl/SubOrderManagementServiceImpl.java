package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.PageResponse;
import com.khang.backendecommerce.infrastructure.common.enums.InventoryStatus;
import com.khang.backendecommerce.infrastructure.configuration.CurrentUserProvider;
import com.khang.backendecommerce.newstruc.dto.response.store.OrderItemInSubOrderResponse;
import com.khang.backendecommerce.newstruc.dto.response.store.SubOrderPendingResponse;
import com.khang.backendecommerce.newstruc.entity.OrderEntity;
import com.khang.backendecommerce.newstruc.entity.OrderItem;
import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;
import com.khang.backendecommerce.newstruc.entity.UserEntity;
import com.khang.backendecommerce.newstruc.repo.SubOrderRepository;
import com.khang.backendecommerce.newstruc.service.SubOrderManagementService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CART - SERVICE")
public class SubOrderManagementServiceImpl implements SubOrderManagementService {
   private final CurrentUserProvider currentUserProvider;
   private final SubOrderRepository subOrderRepo;
    @Override
    public BaseResponse<?> getAllPendingSuborders(Pageable pageable) {
        UserEntity user  = currentUserProvider.getCurrentUser();
        Page<SubOrderEntity> pendingSubOrder = subOrderRepo.findPendingBySellerId(user.getId(),  pageable);
        PageResponse<?> page =  convertToPageResponse(pendingSubOrder, pageable);
    }

    private PageResponse<?> convertToPageResponse(Page<SubOrderEntity> pendingSubOrder, Pageable pageable) {
        List<SubOrderPendingResponse> responses= pendingSubOrder.stream()
                .flatMap(subOrder -> subOrder.getOrderItems()
                                .stream().map(orderItem -> {
                            return OrderItemInSubOrderResponse.builder()
                                            .productName(orderItem.getProductName())
                                            .quantity(orderItem.getQuantity())
                                            .inventoryStatus(orderItem.getInventory().getInventoryStatus())
                                            .totalAmount(orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                                            .build();
                                }).toList().stream().map(orderItemInSubOrderResponse -> orderItem)


//            List<OrderItemInSubOrderResponse> orderItem = pendingSubOrder.stream().map(
//                    subOrder1 -> {
//
//                                    return OrderItemInSubOrderResponse.builder()
//                                            .productName(orderItem1.getProductName())
//                                            .quantity(orderItem1.getQuantity())
//                                            .inventoryStatus(orderItem1.getInventory().getInventoryStatus())
//                                            .totalAmount(orderItem1.getUnitPrice().multiply(BigDecimal.valueOf(orderItem1.getQuantity())))
//                                            .build();
//                                }).toList();
//                    }).toList();
//








//                   return  SubOrderPendingResponse.builder()
//                            .orderId(order.getId())
//                            .orderCode(order.getOrderCode())
//                            .createdAt(order.getCreatedAt())
//                           .customerName(order.getCustomerName())
//                           .items(orderItem).shippingAddress(order.getAddress())
//                           .totalAmount(order.getSubtotal())
//                           .paymentMethod(order.getPaymentMethod())
//                           .paymentStatus(order.getPaymentStatus())
//                            .build();
//
//        }).toList();

    }
}
