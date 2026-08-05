package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.newstruc.domain.order.dto.AllocatedItem;
import com.khang.backendecommerce.newstruc.entity.CartItemEntity;
import com.khang.backendecommerce.newstruc.entity.DeliveryFeeEntity;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface InventoryService {
//  void  checkProductQuantityUpdate(int quantityUpdate, int inventoryQuantity );
    InventoryEntity checkProductExistingInventory(String productId );

  InventoryEntity findProductAvailability(ProductEntity product, int quantity);

     Map<String, List<InventoryEntity>>  loadAndLockInventories(List<CartItemEntity> cartItemList);

     Set<String> extractWarehouseStateIds(Map<String, List<InventoryEntity>> productByInventories);


    InventoryEntity selectInventory(ProductEntity product, int productQuantity, List<InventoryEntity> inventoryEntities, Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehouseStateId,String userStateId);
}
