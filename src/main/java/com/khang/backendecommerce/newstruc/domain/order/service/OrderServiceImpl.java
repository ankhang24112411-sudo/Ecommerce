package com.khang.backendecommerce.newstruc.domain.order.service;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.infrastructure.common.enums.PaymentMethod;
import com.khang.backendecommerce.infrastructure.common.enums.PaymentStatus;
import com.khang.backendecommerce.infrastructure.configuration.CurrentUserProvider;
import com.khang.backendecommerce.infrastructure.discountinfra.DiscountContext;
import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.newstruc.domain.order.dto.AllocatedItem;
import com.khang.backendecommerce.newstruc.domain.order.dto.request.OrderRequest;
import com.khang.backendecommerce.newstruc.domain.order.dto.response.OrderResponse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

        List<AllocatedItem> allocatedItemList = findAllocateAndLock(cartItemList,inventoriesByProduct, warehouseIds,deliveryFeeEntityByWarehouseStateId, state.getId() );
         OrderEntity newOrder = createOrder(allocatedItemList, user, discountCustomer);
//         List<SubOrderEntity> subOrderList = createSubOrder(allocatedItemList, newOrder);
//
//         List<OrderItem> orderItemList = createOrderItem(allocatedItemList,subOrderList);
//         if(request.getPaymentMethod().equals(PaymentMethod.COD)){
//             return handleOrderCODpaymentMethod(newOrder, subOrderList);
//         }
        return null;

    }

//    private List<OrderItem> createOrderItem(List<AllocatedItem> allocatedItemList, List<SubOrderEntity> subOrderList) {
//        return allocatedItemList.stream().map( allocatedItem -> OrderItem.builder()
//                .subOrder(allocatedItem.)
//                .product()
//                .store()
//                .productName()
//                .sku()
//                .unitPrice()
//                .quantity()
//                .build());
//    }

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
    }

    private OrderEntity createOrder(List<AllocatedItem> allocatedItemList, UserEntity user, DiscountCustomerEntity discountCustomer) {
        BigDecimal discountCalculate = BigDecimal.ZERO;
        BigDecimal orderSubTotal = allocatedItemList.stream()
                .map(AllocatedItem::subtotal)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        BigDecimal totalDeliveryAmount = allocatedItemList.stream()
                .map(AllocatedItem::deliveryFee)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        DiscountContext context = DiscountContext.builder()
                .subtotal(orderSubTotal)
                .deliveryAmount(totalDeliveryAmount)
                .build();
        if(discountCustomer != null) {
             discountCalculate = discountService.calculateDiscount(discountCustomer, context);
        }
        BigDecimal totalFinalAmount = orderSubTotal.add(totalDeliveryAmount).subtract(discountCalculate);
       OrderEntity orderEntity = OrderEntity.builder()
               .customer(user)
               .customerName(user.getFullName())
               .state(user.getState())
               .subtotal(orderSubTotal)
               .discountQuantity(1)
               .discountTotalAmount(discountCalculate)
               .orderTotalAmount(totalFinalAmount)
               .build();

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

               SubOrderEntity subOrder = SubOrderEntity.builder()
                       .order(orderEntity).store(storeItems.get(0).product().getStore())
                       .deliveryRoute(storeItems.).deliveryFee().orderItems()
                       .build();

               List<OrderItem> orderItemList = storeItems.stream()
                       .map(item -> OrderItem.builder()
                               .subOrder(subOrder)
                               .product(item.product())
                               .store(subOrder.getStore())
                               .productName(item.product().getName())
                               .sku(item.product().getSku())
                               .unitPrice(item.unitPrice())
                               .quantity(item.quantity())
                               .build()).toList();
               orderItemList.forEach(subOrder::addOrderItems);


           }).toList();

    }

    @Override
    public List<AllocatedItem> findAllocateAndLock(List<CartItemEntity> cartItemList,Map<String, List<InventoryEntity>> inventoriesByProduct, Set<String> warehouseIds,
                                                   Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehouseStateId,
                                                   String userStateId) {
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
            selectInventory.updateReservedQuantityAndAvailableQuantity(productQuantity);

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
