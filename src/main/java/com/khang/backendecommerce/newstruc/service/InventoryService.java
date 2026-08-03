package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.newstruc.domain.order.dto.AllocatedItem;
import com.khang.backendecommerce.newstruc.entity.CartItemEntity;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;

import java.util.List;
import java.util.Map;

public interface InventoryService {
//  void  checkProductQuantityUpdate(int quantityUpdate, int inventoryQuantity );
    InventoryEntity checkProductExistingInventory(String productId );

  InventoryEntity findProductAvailability(ProductEntity product, int quantity);

  List<AllocatedItem> findAllocateAndLock (List<CartItemEntity> cartItemList, String stateId);
     Map<String, List<InventoryEntity>>  loadAndLockInventories(List<CartItemEntity> cartItemList);
}
