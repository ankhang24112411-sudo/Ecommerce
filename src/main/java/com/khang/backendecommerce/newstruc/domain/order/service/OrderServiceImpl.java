package com.khang.backendecommerce.newstruc.domain.order.service;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.infrastructure.common.enums.PaymentMethod;
import com.khang.backendecommerce.infrastructure.common.enums.PaymentStatus;
import com.khang.backendecommerce.infrastructure.configuration.CurrentUserProvider;
import com.khang.backendecommerce.infrastructure.discountinfra.DiscountContext;
import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.infrastructure.util.AppConst;
import com.khang.backendecommerce.newstruc.domain.order.dto.AllocatedItem;
import com.khang.backendecommerce.newstruc.domain.order.dto.request.OrderRequest;
import com.khang.backendecommerce.newstruc.domain.order.dto.response.OrderItemResponse;
import com.khang.backendecommerce.newstruc.domain.order.dto.response.OrderResponse;
import com.khang.backendecommerce.newstruc.domain.order.dto.response.SubOrderResponse;
import com.khang.backendecommerce.newstruc.entity.UserEntity;
import com.khang.backendecommerce.newstruc.entity.*;
import com.khang.backendecommerce.newstruc.repo.*;
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
    private final UserRepository userRepo;
    private final PaymentRepository paymentRepo;
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
        String userId = currentUserProvider.getCurrentUser().getId();
        UserEntity user = userRepo.findByIdWithState(userId).orElseThrow(() -> ApplicationErrors.USER_NOT_FOUND);
        log.info("Creating order for userId: {}", user.getId());

        StateEntity state = user.getState();
        CartEntity cart = cartService.findByUserId(user.getId());
        if(cart == null) {
            log.error("Cart not found for userId: {}", user.getId());
        }
        DiscountCustomerEntity discountCustomer = cart.getDiscount();
        DiscountEntity discount = discountCustomer == null ? null : discountService.findAndCheckDiscountCustomer(discountCustomer);

        List<CartItemEntity> cartItemList = cartService.loadCartItems(cart, user);
        Map<String, List<InventoryEntity>> inventoriesByProduct = inventoryService.loadAndLockInventories(cartItemList);

//        inventoriesByProduct.entrySet().stream()
//                .map(entry -> Map.entry(
//                        entry.getKey(),
//                        entry.getValue().stream()
//                                .map(inventory -> inventory.getWarehouse())
//                                .toList()
//                ))
//                .toList()
        Set<String> warehouseStateIds = inventoryService.extractWarehouseStateIds(inventoriesByProduct);
        Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehouseStateId = deliveryService.deliveryFeeEntityByWarehousesStateId(warehouseStateIds, state.getId());

        List<AllocatedItem> allocatedItemList = findAllocateAndLock(cartItemList, inventoriesByProduct, warehouseStateIds, deliveryFeeEntityByWarehouseStateId, state.getId(), request.getPaymentMethod());
        return createOrder(allocatedItemList, user, discountCustomer,request.getPaymentMethod());

    }

    private OrderResponse handleOrderCODpaymentMethod(OrderEntity newOrder, List<SubOrderEntity> subOrderList) {
        newOrder.setPaymentMethod(PaymentMethod.COD);
        newOrder.setConfirmedAt(Instant.now());
        newOrder.setPaymentStatus(PaymentStatus.UNPAID);
        newOrder.setOrderStatus(OrderStatus.PENDING);

        subOrderList.forEach(subOrder -> subOrder.setOrderStatus(OrderStatus.PENDING));
        subOrderList.forEach(newOrder::addSubOrder);
        orderRepo.save(newOrder);
        return convertToOrderResponse(null,newOrder,subOrderList);
    }

    private OrderResponse convertToOrderResponse(String paymentRefence ,OrderEntity newOrder, List<SubOrderEntity> subOrderList) {
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
                  newOrder.getOrderCode(),
                  newOrder.getOrderStatus(),
                  newOrder.getPaymentStatus(),
                  newOrder.getPaymentMethod() == PaymentMethod.COD ? "COD" : paymentRefence,
                  newOrder.getSubtotal(),
                  subOrderList.stream().map(SubOrderEntity::getDeliveryFee).reduce(BigDecimal.ZERO, BigDecimal::add),
                  newOrder.getDiscountTotalAmount(),
                  newOrder.getOrderTotalAmount(),
                  newOrder.getCreatedAt(),
                  subOrderResponses
          );
      }


    private OrderResponse createOrder(List<AllocatedItem> allocatedItemList, UserEntity user, DiscountCustomerEntity discountCustomer, PaymentMethod paymentMethod) {

       OrderEntity orderEntity = OrderEntity.builder()
               .customer(user)
               .customerName(user.getFullName())
               .state(user.getState()).build();

        Map<SubOrderGroupKey, List<AllocatedItem>> groupedItems =
                allocatedItemList.stream().collect(Collectors.groupingBy(item ->
                                new SubOrderGroupKey(item.product().getStore().getId(), item.inventory().getWarehouse().getId(),
                                        item.deliveryRoute().getId())
                        ));

   List<SubOrderEntity> subOrderList = groupedItems
           .values()
           .stream()
           .map(storeItems -> {

               AllocatedItem firstItem = storeItems.get(0);
               SubOrderEntity subOrder = SubOrderEntity.builder()
                       .order(orderEntity)
                       .store(firstItem.product().getStore())
                       .deliveryRoute(firstItem.deliveryRoute())
                       .deliveryFee(firstItem.deliveryFee())
                       .build();

               List<OrderItem> orderItemList = storeItems.stream()
                       .peek(item -> {
                           log.info("Before mapping : product {}", item.product());
                       })
                       .map(item -> OrderItem.builder()
                               .product(item.product())
                               .productName(item.product().getName())
                               .sku(item.product().getSku())
                               .unitPrice(item.unitPrice())
                               .quantity(item.quantity())
                               .build()).toList();

               orderItemList.forEach(subOrder::addOrderItems);

               log.info(
                       "After mapping: product Name{}",
                       orderItemList.stream().map(OrderItem::getProduct)
                               .map(ProductEntity::getName)
               );



               BigDecimal subOrderTotal = orderItemList.stream()
                       .map(orderItem -> orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                       .reduce(BigDecimal.ZERO,BigDecimal::add);

               subOrder.setSubTotal(subOrderTotal);


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
        orderEntity.setDeliveryFee(totalDeliveryAmountNoDuplicate);
        orderEntity.setDiscountTotalAmount(discountCalculate);
        orderEntity.setOrderTotalAmount(totalFinalAmount);
        orderEntity.setOrderCode(generateOrderCode());

        if(paymentMethod.equals(PaymentMethod.COD)){
            return handleOrderCODpaymentMethod(orderEntity, subOrderList);
        }
          return handleOrderOnlineBanking(user ,orderEntity, subOrderList);
    }

    private OrderResponse handleOrderOnlineBanking(UserEntity user , OrderEntity newOrder, List<SubOrderEntity> subOrderList) {
        newOrder.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
        newOrder.setConfirmedAt(Instant.now());
        newOrder.setOrderStatus(OrderStatus.PENDING);
        newOrder.setPaymentStatus(PaymentStatus.UNPAID);

        PaymentEntity payment = PaymentEntity.builder()
                .user(user)
                .order(newOrder)
                .paymentReference(AppConst.paymentReference)
                .build();
        paymentRepo.save(payment);
        subOrderList.forEach(subOrder -> subOrder.setOrderStatus(OrderStatus.PENDING));
        subOrderList.forEach(newOrder::addSubOrder);
        orderRepo.save(newOrder);
        return convertToOrderResponse(payment.getPaymentReference(),newOrder,subOrderList);
    }

    @Override
    public List<AllocatedItem> findAllocateAndLock(List<CartItemEntity> cartItemList,
                                                   Map<String, List<InventoryEntity>> inventoriesByProduct,
                                                   Set<String> warehouseStateIds,
                                                   Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehouseStateId,
                                                   String userStateId, PaymentMethod paymentMethod) {
        List<AllocatedItem> result = new ArrayList<>();
        for(CartItemEntity cartItem : cartItemList){
            ProductEntity product = cartItem.getProduct();
            int productQuantity = cartItem.getQuantity();
            String productId = product.getId();

            List<InventoryEntity> inventoryEntities = inventoriesByProduct.get(productId);
            InventoryEntity selectInventory = inventoryService.selectInventory(product ,productQuantity,inventoryEntities, deliveryFeeEntityByWarehouseStateId,  userStateId);
            log.info("inventory is ={} in state {}" , selectInventory , selectInventory.getWarehouse().getState());
            if(selectInventory == null){
                throw ApplicationErrors.INVENTORY_NOT_FOUND;
            }

            if(paymentMethod.equals(PaymentMethod.COD)){
                selectInventory.updateQuantityWhenPaymentSuccessOrCOD(productQuantity);
            }
            else {
                selectInventory.updateReservedQuantityAndAvailableQuantity(productQuantity);
            }

            DeliveryRouteEntity deliveryRoute = deliveryRouteRepo.findByStateFrom_IdAndStateTo_Id(selectInventory.getWarehouse().getState().getId(),userStateId).orElseThrow(() -> ApplicationErrors.DELIVERY_ROUTE_NOT_FOUND);
          log.info("delivery route is = {}" , deliveryRoute);
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
