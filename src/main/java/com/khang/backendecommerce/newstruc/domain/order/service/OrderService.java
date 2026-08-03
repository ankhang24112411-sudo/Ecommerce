package com.khang.backendecommerce.newstruc.domain.order.service;

import com.khang.backendecommerce.newstruc.domain.order.dto.AllocatedItem;
import com.khang.backendecommerce.newstruc.domain.order.dto.request.OrderRequest;
import com.khang.backendecommerce.newstruc.domain.order.dto.response.OrderResponse;
import com.khang.backendecommerce.newstruc.entity.CartItemEntity;
import com.khang.backendecommerce.newstruc.entity.DeliveryFeeEntity;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest request);
    public List<AllocatedItem> findAllocateAndLock(List<CartItemEntity> cartItemList,
                                                   Map<String, List<InventoryEntity>> inventoriesByProduct,
                                                   Set<String> warehouseIds,
                                                   Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehouseStateId,
                                                   String userStateId);

}
