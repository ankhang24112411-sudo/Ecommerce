package com.khang.backendecommerce.newstruc.domain.order.service;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.infrastructure.common.enums.PaymentMethod;
import com.khang.backendecommerce.infrastructure.common.enums.PaymentStatus;
import com.khang.backendecommerce.infrastructure.configuration.CurrentUserProvider;
import com.khang.backendecommerce.infrastructure.discountinfra.DiscountContext;
import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.newstruc.domain.order.dto.AllocatedItem;
import com.khang.backendecommerce.newstruc.domain.order.dto.request.OrderRequest;
import com.khang.backendecommerce.newstruc.domain.order.dto.response.OrderItemResponse;
import com.khang.backendecommerce.newstruc.domain.order.dto.response.OrderResponse;
import com.khang.backendecommerce.newstruc.domain.order.dto.response.SubOrderResponse;
import com.khang.backendecommerce.newstruc.entity.UserEntity;
import com.khang.backendecommerce.newstruc.entity.*;
import com.khang.backendecommerce.newstruc.repo.DeliveryRouteRepository;
import com.khang.backendecommerce.newstruc.repo.OrderRepository;
import com.khang.backendecommerce.newstruc.repo.SubOrderRepository;
import com.khang.backendecommerce.newstruc.service.DeliveryService;
import com.khang.backendecommerce.newstruc.service.CartService;
import com.khang.backendecommerce.newstruc.service.DiscountService;
import com.khang.backendecommerce.newstruc.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ORDER - SERVICE")
public class OrderServiceImpl implements OrderService{
    private final CartService cartService;
    private final DiscountService discountService;
    private final CurrentUserProvider currentUserProvider;
    private final InventoryService inventoryService;
    private final DeliveryService deliveryService;
    private final SubOrderRepository subOrderRepo;
    private final DeliveryRouteRepository deliveryRouteRepo;
    private final OrderRepository orderRepo;
    private static final DateTimeFormatter ORDER_CODE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    private String generateOrderCode() {
        String date = LocalDate.now().format(ORDER_CODE_FORMAT);

        String randomPart = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();

        return "ORD-" + date + "-" + randomPart;
    }
    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        UserEntity user = currentUserProvider.getCurrentUser();
        StateEntity state = user.getState();
        CartEntity cart = cartService.findByUserId(user.getId());
        DiscountCustomerEntity discountCustomer = cart.getDiscount();
        DiscountEntity discount = discountCustomer == null ? null : discountService.findAndCheckDiscountCustomer(discountCustomer);

        List<CartItemEntity> cartItemList = cartService.loadCartItems(cart);
        Map<String, List<InventoryEntity>> inventoriesByProduct = inventoryService.loadAndLockInventories(cartItemList);

        Set<String> warehouseIds = inventoryService.extractWarehouseIds(inventoriesByProduct);
        Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehouseStateId = deliveryService.deliveryFeeEntityByWarehousesId(warehouseIds, state.getId());

        List<AllocatedItem> allocatedItemList = findAllocateAndLock(cartItemList, inventoriesByProduct, warehouseIds, deliveryFeeEntityByWarehouseStateId, state.getId(), request.getPaymentMethod());
        OrderEntity newOrder = createOrder(allocatedItemList, user, discountCustomer);
        return null;
    }

    private OrderResponse handleOrderCODpaymentMethod(OrderEntity newOrder, List<SubOrderEntity> subOrderList) {
        newOrder.setPaymentMethod(PaymentMethod.COD);
        newOrder.setConfirmedAt(Instant.now());
        newOrder.setPaymentStatus(PaymentStatus.UNPAID);
        subOrderList.forEach(subOrder -> subOrder.setOrderStatus(OrderStatus.PENDING));
        
        orderRepo.save(newOrder);
        subOrderRepo.saveAll(subOrderList);
        return convertToOrderResponse(newOrder,subOrderList);
    }

    private OrderResponse convertToOrderResponse(OrderEntity newOrder, List<SubOrderEntity> subOrderList) {
//        List<OrderItem> orderItems = subOrderList.stream()
//                .map(SubOrderEntity::getOrderItems)
//                .flatMap(Collection::stream).toList();
//        List<OrderItemResponse> itemResponses = newOrder.getSubOrders().stream()
//                .flatMap(subOrder -> subOrder.getOrderItems().stream())
//                .map(orderItem -> OrderItemResponse.builder()
//                        .orderItemId(orderItem.getId())
//                        .productName(orderItem.getProduct().getName())
//                        .storeName(orderItem.getStore().getName())
//                        .sku(orderItem.getProduct().getSku())
//                        .unitPrice(orderItem.getUnitPrice())
//                        .quantity(orderItem.getQuantity())
//                        .lineTotal(orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
//                        .build()).toList();
        List<SubOrderResponse> subOrderResponses = newOrder.getSubOrders().stream()
                .map(subOrder -> {
                            List<OrderItemResponse> orderItemResponses = subOrder.getOrderItems().stream()
                                    .map(orderItem -> {
                                        return new OrderItemResponse(
                                                orderItem.getId(),
                                                orderItem.getStore().getName(),
                                                orderItem.getProduct().getName(),
                                                orderItem.getSku(),
                                                orderItem.getUnitPrice(),
                                                orderItem.getQuantity(),
                                                orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())
                                                )
                                        );
                                    }).toList();
                            return new SubOrderResponse(
                                    subOrder.getId(),
                                    subOrder.getStoreName(),
                                    subOrder.getOrderStatus(),
                                    subOrder.getSubTotal(),
                                    subOrder.getDeliveryFee(),
                                    orderItemResponses
                            );
                        }).toList();
          return new OrderResponse(
                  newOrder.getId(),
                  newOrder.getPaymentStatus(),
                  newOrder.
          )
      }


    private OrderResponse createOrder(List<AllocatedItem> allocatedItemList, UserEntity user, DiscountCustomerEntity discountCustomer, PaymentMethod paymentMethod) {

       OrderEntity orderEntity = OrderEntity.builder()
               .customer(user)
               .customerName(user.getFullName())
               .state(user.getState()).build();


//      List<SubOrderEntity> subOrderEntities =   allocatedItemList.stream()
//                         .map(allocatedItem -> SubOrderEntity.builder()
//                        .order(orderEntity)
//                        .store(allocatedItem.product().getStore())
//                        .subTotal(allocatedItem.subtotal())
//                        .deliveryRoute(allocatedItem.deliveryRoute())
//                        .build()).toList();
//
//      subOrderEntities.forEach(orderEntity::addSubOrder);
//
//        return allocatedItemList.stream().map( allocatedItem -> OrderItem.builder()
//                .subOrder(allocatedItem.)
//                .product()
//                .store()
//                .productName()
//                .sku()
//                .unitPrice()
//                .quantity()
//                .build());
   List<SubOrderEntity> subOrderList = allocatedItemList.stream()
           .collect(Collectors.groupingBy(allocatedItem -> allocatedItem.product().getStore().getId()))
           .values()
           .stream()
           .map(storeItems -> {

               List<OrderItem> orderItemList = storeItems.stream()
                       .map(item -> OrderItem.builder()
                               .product(item.product())
                               .store(item.product().getStore())
                               .productName(item.product().getName())
                               .sku(item.product().getSku())
                               .unitPrice(item.unitPrice())
                               .quantity(item.quantity())
                               .build()).toList();

               BigDecimal subOrderTotal = orderItemList.stream()
                       .map(orderItem -> orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                       .reduce(BigDecimal.ZERO,BigDecimal::add);

               SubOrderEntity subOrder = SubOrderEntity.builder()
                       .order(orderEntity)
                       .store(storeItems.get(0).product().getStore())
                       .deliveryRoute(storeItems.get(0).deliveryRoute())
                       .deliveryFee(storeItems.get(0).deliveryFee())
                       .subTotal(subOrderTotal)
                       .build();


               orderItemList.forEach(subOrder::addOrderItems);
               return subOrder;
           }).toList();


   BigDecimal totalDeliveryAmountNoDuplicate = subOrderList.stream()
           .map(SubOrderEntity::getDeliveryFee)
           .reduce(BigDecimal.ZERO,BigDecimal::add);

        BigDecimal discountCalculate = BigDecimal.ZERO;

        BigDecimal orderSubTotal = allocatedItemList.stream()
                .map(AllocatedItem::subtotal)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        DiscountContext context = DiscountContext.builder()
                .subtotal(orderSubTotal)
                .deliveryAmount(totalDeliveryAmountNoDuplicate)
                .build();
        if(discountCustomer != null) {
            discountCalculate = discountService.calculateDiscount(discountCustomer, context);
        }

        BigDecimal totalFinalAmount = orderSubTotal.add(totalDeliveryAmountNoDuplicate).subtract(discountCalculate);

        orderEntity.setSubtotal(orderSubTotal);
        orderEntity.setDiscountTotalAmount(discountCalculate);
        orderEntity.setOrderTotalAmount(totalFinalAmount);
        orderEntity.setOrderCode(generateOrderCode());

        if(paymentMethod.equals(PaymentMethod.COD)){
            return handleOrderCODpaymentMethod(orderEntity);
        }

        return convertToOrderResponse(orderEntity,subOrderList);
    }

    @Override
    public List<AllocatedItem> findAllocateAndLock(List<CartItemEntity> cartItemList,Map<String, List<InventoryEntity>> inventoriesByProduct, Set<String> warehouseIds,
                                                   Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehouseStateId,
                                                   String userStateId, PaymentMethod paymentMethod) {
        List<AllocatedItem> result = new ArrayList<>();
        for(CartItemEntity cartItem : cartItemList){
            ProductEntity product = cartItem.getProduct();
            int productQuantity = cartItem.getQuantity();
            String productId = product.getId();

            List<InventoryEntity> inventoryEntities = inventoriesByProduct.get(productId);
            InventoryEntity selectInventory = inventoryService.selectInventory(product ,productQuantity,inventoryEntities, deliveryFeeEntityByWarehouseStateId,  userStateId);
            if(selectInventory == null){
                throw ApplicationErrors.INVENTORY_NOT_FOUND;
            }

            if(paymentMethod.equals(PaymentMethod.COD)){
                selectInventory.updateQuantityWhenPaymentSuccessOrCOD(productQuantity);
            }
            else {
                selectInventory.updateReservedQuantityAndAvailableQuantity(productQuantity);
            }

            DeliveryRouteEntity deliveryRoute = deliveryRouteRepo.findByStateFrom_IdAndStateTo_Id(selectInventory.getWarehouse().getId(),userStateId).orElseThrow(() -> ApplicationErrors.DELIVERY_ROUTE_NOT_FOUND);

            BigDecimal fee = deliveryService.calculateDeliveryFee(selectInventory, userStateId,deliveryFeeEntityByWarehouseStateId);
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(productQuantity));
            AllocatedItem allocatedItem = AllocatedItem.builder()
                    .deliveryRoute(deliveryRoute)
                    .cartItem(cartItem)
                    .product(product)
                    .inventory(selectInventory)
                    .quantity(productQuantity)
                    .unitPrice(product.getPrice())
                    .subtotal(subtotal)
                    .deliveryFee(fee)
                    .build();
            result.add(allocatedItem);

        }
        return result;
    }

}
