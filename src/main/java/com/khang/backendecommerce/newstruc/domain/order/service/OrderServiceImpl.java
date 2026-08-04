package com.khang.backendecommerce.newstruc.domain.order.service;

import com.khang.backendecommerce.infrastructure.configuration.CurrentUserProvider;
import com.khang.backendecommerce.infrastructure.discountinfra.DiscountContext;
import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.newstruc.domain.order.dto.AllocatedItem;
import com.khang.backendecommerce.newstruc.domain.order.dto.request.OrderRequest;
import com.khang.backendecommerce.newstruc.domain.order.dto.response.OrderResponse;
import com.khang.backendecommerce.newstruc.entity.UserEntity;
import com.khang.backendecommerce.newstruc.entity.*;
import com.khang.backendecommerce.newstruc.service.DeliveryService;
import com.khang.backendecommerce.newstruc.service.CartService;
import com.khang.backendecommerce.newstruc.service.DiscountService;
import com.khang.backendecommerce.newstruc.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ORDER - SERVICE")
public class OrderServiceImpl implements OrderService{
    private final CartService cartService;
    private final DiscountService discountService;
    private final CurrentUserProvider currentUserProvider;
    private final InventoryService inventoryService;
    private final DeliveryService deliveryService;
    private final SubOrder
    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        UserEntity user = currentUserProvider.getCurrentUser();
        StateEntity state = user.getState();
        CartEntity cart = cartService.findByUserId(user.getId());
        DiscountCustomerEntity discountCustomer = cart.getDiscount();
        if(discountCustomer != null) {
            DiscountEntity discount = discountService.findAndCheckDiscountCustomer(discountCustomer);
        }
        List<CartItemEntity> cartItemList = cartService.loadCartItems(cart);
        Map<String, List<InventoryEntity>> inventoriesByProduct = inventoryService.loadAndLockInventories(cartItemList);
        
        Set<String> warehouseIds = inventoryService.extractWarehouseIds(inventoriesByProduct);
        Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehouseStateId = deliveryService.deliveryFeeEntityByWarehousesId(warehouseIds, state.getId());

        List<AllocatedItem> allocatedItemList = findAllocateAndLock(cartItemList,inventoriesByProduct, warehouseIds,deliveryFeeEntityByWarehouseStateId, state.getId() );
         OrderEntity newOrder = createOrder(allocatedItemList, user);


        return null;

    }

    private OrderEntity createOrder(List<AllocatedItem> allocatedItemList, UserEntity user, DiscountCustomerEntity discountCustomer) {
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
        BigDecimal discountCalculate = discountService.calculateDiscount(discountCustomer, context);

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
      List<SubOrderEntity>  listSubOrder = allocatedItemList.stream().map(subOrder -> SubOrderEntity.builder()
                .order()
                .store()
                .orderStatus()
                .storeName()
                .subTotal()
                .confirmedAt()
                .rejectedAt()
                .rejectionReason()
                .build());
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

            BigDecimal fee = deliveryService.calculateDeliveryFee(selectInventory, userStateId,deliveryFeeEntityByWarehouseStateId);
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(productQuantity));
            AllocatedItem allocatedItem = AllocatedItem.builder()
                    .cartItem(cartItem)
                    .product(product)
                    .inventory(selectInventory).quantity(productQuantity)
                    .unitPrice(product.getPrice())
                    .subtotal(subtotal)
                    .deliveryFee(fee)
                    .build();
            result.add(allocatedItem);

        }
        return result;
    }
    private void createSubOrder(List<AllocatedItem> listSubOrder){

    }
}
