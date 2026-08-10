package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.PageResponse;
import com.khang.backendecommerce.infrastructure.common.enums.InventoryStatus;
import com.khang.backendecommerce.infrastructure.common.enums.OrderResult;
import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.infrastructure.configuration.CurrentUserProvider;
import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.newstruc.dto.response.store.OrderItemInSubOrderResponse;
import com.khang.backendecommerce.newstruc.dto.response.store.SubOrderPendingResponse;
import com.khang.backendecommerce.newstruc.dto.response.store.SubOrderStatusResponse;
import com.khang.backendecommerce.newstruc.entity.*;
import com.khang.backendecommerce.newstruc.repo.SubOrderRepository;
import com.khang.backendecommerce.newstruc.service.SubOrderManagementService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
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
        return    BaseResponse.ofSuccess(page);

    }
    private UserEntity validateShopOwnerToSuborder(String subOrderId){
        UserEntity user = currentUserProvider.getCurrentUser();
        if(!subOrderRepo.existsByIdAndStore_Owner_Id(subOrderId, user.getId())){
            throw ApplicationErrors.ACCESS_DENIED;
        }
        return user;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SubOrderStatusResponse confirmSubOrdersStatus( String subOrderId) {
      UserEntity user = validateShopOwnerToSuborder( subOrderId);

      SubOrderEntity subOrder = subOrderRepo.findSubOrder(user.getId(), subOrderId);
      if(!subOrder.getOrderStatus().equals(OrderStatus.PENDING)){
          throw ApplicationErrors.INVALID_ORDER_STATUS;
      }
      subOrder.setOrderStatus(OrderStatus.CONFIRMED);
        return SubOrderStatusResponse.builder()
                .subOrderId(subOrderId)
                .orderStatus(OrderStatus.CONFIRMED)
                .confirmedAt(Instant.now())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SubOrderStatusResponse rejectSubOrders(String subOrderId) {
        UserEntity user = validateShopOwnerToSuborder( subOrderId);
        SubOrderEntity subOrder = subOrderRepo.findSubOrder(user.getId(), subOrderId);
      if(!subOrder.getOrderStatus().equals(OrderStatus.PENDING)){
          throw ApplicationErrors.INVALID_ORDER_STATUS;
      }

      subOrder.setOrderStatus(OrderStatus.FAILED);
      subOrder.setRejectedAt(Instant.now());
      subOrder.setRejectionReason("REJECT FROM SHOP OWNER");
       subOrder.getOrderItems()
              .forEach(orderItem ->{
             InventoryEntity inventory =  orderItem.getInventory();
             inventory.updateQuantityWhenSubOrderRejectOrRefund(orderItem.getQuantity());
      });
    OrderEntity order = subOrder.getOrder();
    BigDecimal payableAmount = order.getOrderTotalAmount().min(subOrder.getSubTotal().add(subOrder.getDeliveryFee()));
    order.setPayableAmount(payableAmount);
    order.setOrderResult(OrderResult.PARTIAL_SUCCESS);

        return null;
    }

    private PageResponse<?> convertToPageResponse(Page<SubOrderEntity> pendingSubOrder, Pageable pageable) {
        List<OrderItemInSubOrderResponse> responses= pendingSubOrder.stream()
                .flatMap(subOrder -> subOrder.getOrderItems()
                                .stream().map(orderItem ->
                                     OrderItemInSubOrderResponse.builder()
                                            .productName(orderItem.getProductName())
                                            .quantity(orderItem.getQuantity())
                                            .inventoryStatus(orderItem.getInventory().getInventoryStatus())
                                            .totalAmount(orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                                            .build()
                                )
                ).toList();
     List<SubOrderPendingResponse> responseList = pendingSubOrder.stream()
             .map(subOrder -> {
                 OrderEntity order = subOrder.getOrder();
                 BigDecimal totalAmount = subOrder.getOrderItems().stream().map(orderItem -> {
                    return orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
                 }).reduce(BigDecimal.ZERO,BigDecimal::add);
                 return SubOrderPendingResponse.builder()
                         .orderId(order.getId())
                         .orderCode(order.getOrderCode())
                         .createdAt(order.getCreatedAt())
                         .customerName(order.getCustomerName())
                         .items(responses)
                         .shippingAddress(order.getAddress())
                         .totalAmount(totalAmount).paymentMethod(order.getPaymentMethod()).paymentStatus(order.getPaymentStatus())
                         .build();
             }).toList();
     long total = responses.size();
        return PageResponse.of(responseList,pageable,total);
    }
}
