package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.newstruc.domain.order.dto.AllocatedItem;
import com.khang.backendecommerce.newstruc.entity.CartItemEntity;
import com.khang.backendecommerce.newstruc.entity.DeliveryFeeEntity;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;
import com.khang.backendecommerce.newstruc.repo.InventoryRepository;
import com.khang.backendecommerce.newstruc.service.InventoryService;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepo;



//    public void checkProductQuantityUpdate(int quantityUpdate, int inventoryQuantity ) {
//
//    }
    public InventoryEntity checkProductExistingInventory(String productId ){
        return inventoryRepo.findByProduct_Id(productId).orElseThrow(() ->  ApplicationErrors.PRODUCT_EXISTED);

    }

    @Override
    public InventoryEntity findProductAvailability(ProductEntity product, int quantity) {
        if(product.getDeleted() == 1){
            throw ApplicationErrors.PRODUCT_INACTIVE;
        }
        InventoryEntity inventory = checkProductExistingInventory(product.getId());
        if(inventory.getQuantity() - quantity < 0){
              throw ApplicationErrors.INVENTORY_NOT_ENOUGH;
        }
        return inventory;
    }




    @Override
    public Map<String, List<InventoryEntity>> loadAndLockInventories(List<CartItemEntity> cartItemList) {
        List<String> productIds = cartItemList.stream()
                .map(item -> item.getProduct().getId())
                .toList();
        List<InventoryEntity> inventoryLists = inventoryRepo.findAllInventoryCandidates(productIds)
                .stream()
                .filter( inventory-> inventory.getQuantity() > 0)
                .toList();
        Map<String,List<InventoryEntity>> inventoryByProduct = inventoryLists.stream()
                .collect(Collectors.groupingBy(inventory -> inventory.getProduct().getId()));

        return inventoryByProduct;
    }

    @Override
    public Set<String> extractWarehouseIds(Map<String, List<InventoryEntity>> productByInventories) {
        return productByInventories.values().stream()
                .flatMap(inventories -> inventories.stream())
                .map(inventory -> inventory.getWarehouse().getId())
                .collect(Collectors.toSet());
    }

    @Override
    public InventoryEntity selectInventory(ProductEntity product,
                                           int productQuantity,
                                           List<InventoryEntity> inventoryEntities,
                                           Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehouseStateId,
                                           String userStateId) {
        if(inventoryEntities.isEmpty()){
            throw ApplicationErrors.INVENTORY_NOT_FOUND;
        }
        List<InventoryEntity> candidates = inventoryEntities.stream()
                .filter(inventory -> canDeliveryFromWarehouse(
                        inventory,deliveryFeeEntityByWarehouseStateId, userStateId
                )).toList();


        return null;
    }
    public boolean canDeliveryFromWarehouse(InventoryEntity inventory ,
                                            Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehouseStateId,
                                            String userStateId
                                            ){
        String wareHouseStateId = inventory.getWarehouse().getState().getId();
        if(wareHouseStateId.equals(userStateId)){
            return true;
        }
        return deliveryFeeEntityByWarehouseStateId.containsKey(wareHouseStateId);
    }
}
