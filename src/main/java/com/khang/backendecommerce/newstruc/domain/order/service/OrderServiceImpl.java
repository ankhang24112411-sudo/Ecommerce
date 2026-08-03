package com.khang.backendecommerce.newstruc.domain.order.service;

import com.khang.backendecommerce.infrastructure.configuration.CurrentUserProvider;
import com.khang.backendecommerce.newstruc.domain.order.dto.AllocatedItem;
import com.khang.backendecommerce.newstruc.domain.order.dto.request.OrderRequest;
import com.khang.backendecommerce.newstruc.domain.order.dto.response.OrderResponse;
import com.khang.backendecommerce.newstruc.domain.user.entity.UserEntity;
import com.khang.backendecommerce.newstruc.entity.*;
import com.khang.backendecommerce.newstruc.repo.DeliveryService;
import com.khang.backendecommerce.newstruc.service.CartService;
import com.khang.backendecommerce.newstruc.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ORDER - SERVICE")
public class OrderServiceImpl implements OrderService{
    private final CartService cartService;
    private final CurrentUserProvider currentUserProvider;
    private final InventoryService inventoryService;
    private final DeliveryService deliveryService;
    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        UserEntity user = currentUserProvider.getCurrentUser();
        StateEntity state = user.getState();
        List<CartItemEntity> cartItemList = cartService.loadCartItems(user);
        Map<String, List<InventoryEntity>> inventoriesByProduct = inventoryService.loadAndLockInventories(cartItemList);
        Set<String> warehouseIds = inventoryService.extractWarehouseIds(inventoriesByProduct);
        Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehouseStateId = deliveryService.deliveryFeeEntityByWarehousesId(warehouseIds, state.getId());

        return null;

    }
    @Override
    public List<AllocatedItem> findAllocateAndLock(List<CartItemEntity> cartItemList,
                                                   Map<String, List<InventoryEntity>> inventoriesByProduct,
                                                   Set<String> warehouseIds,
                                                   Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehouseStateId,
                                                   String userStateId) {
        for(CartItemEntity cartItem : cartItemList){
            ProductEntity product = cartItem.getProduct();
            int productQuantity = cartItem.getQuantity();
            String productId = product.getId();
            List<InventoryEntity> inventoryEntities = inventoriesByProduct.get(productId);
            InventoryEntity selectInventory = inventoryService.selectInventory(product ,productQuantity,inventoryEntities, deliveryFeeEntityByWarehouseStateId,  userStateId);
        }
        return List.of();
    }
}
