package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.newstruc.domain.order.dto.AllocatedItem;
import com.khang.backendecommerce.newstruc.dto.response.InventoryNewCartContext;
import com.khang.backendecommerce.newstruc.dto.response.store.CreateProductRequest;
import com.khang.backendecommerce.newstruc.dto.response.store.ProductResponse;
import com.khang.backendecommerce.newstruc.entity.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface InventoryService {
//  void  checkProductQuantityUpdate(int quantityUpdate, int inventoryQuantity );
    InventoryEntity checkProductExistingInventory(String productId );

   InventoryNewCartContext findProductAvailability(ProductEntity product, int quantity, UserEntity user);

     Map<String, List<InventoryEntity>>  loadAndLockInventories(List<CartItemEntity> cartItemList);

     Set<String> extractWarehouseStateIds(Map<String, List<InventoryEntity>> productByInventories);


    InventoryEntity selectInventory(ProductEntity product, int productQuantity, List<InventoryEntity> inventoryEntities, Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehouseStateId,String userStateId);

     Map<String, List<InventoryEntity>> loadInventories(List<CartItemEntity> cartItemList) ;
    Map<String, List<InventoryEntity>> findOptimizeInventory(CartEntity cart ,ProductEntity product, int quantity, Map<String, List<InventoryEntity>> inventoriesByProductId);

    ProductResponse createProduct(CreateProductRequest request);
}
