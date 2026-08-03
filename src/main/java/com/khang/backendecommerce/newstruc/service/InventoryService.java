package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.newstruc.entity.InventoryEntity;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;

public interface InventoryService {
//  void  checkProductQuantityUpdate(int quantityUpdate, int inventoryQuantity );
    InventoryEntity checkProductExistingInventory(String productId );

  InventoryEntity findProductAvailability(ProductEntity product, int quantity);
}
