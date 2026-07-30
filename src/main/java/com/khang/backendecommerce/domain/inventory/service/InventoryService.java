package com.khang.backendecommerce.domain.inventory.service;

import com.khang.backendecommerce.domain.cart.entity.CartItemEntity;
import com.khang.backendecommerce.domain.inventory.entity.InventoryEntity;
import com.khang.backendecommerce.domain.product.entity.ProductEntity;

public interface InventoryService {
  void  checkProductQuantityUpdate(int quantityUpdate, int inventoryQuantity );
    InventoryEntity checkProductExistingInventory(String productId );

  InventoryEntity findProductAvailability(ProductEntity product, int quantity);
}
