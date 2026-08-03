package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.newstruc.domain.order.dto.AllocatedItem;
import com.khang.backendecommerce.newstruc.entity.CartItemEntity;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;
import com.khang.backendecommerce.newstruc.repo.InventoryRepository;
import com.khang.backendecommerce.newstruc.service.InventoryService;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public List<AllocatedItem> findAllocateAndLock(List<CartItemEntity> cartItemList, String stateId) {

//            List<String> productIds = cartItemList

        return List.of();
    }

    @Override
    public Map<String, List<InventoryEntity>> loadAndLockInventories(List<CartItemEntity> cartItemList) {
        List<String> productIds = cartItemList.stream()
                .map(item -> item.getProduct().getId())
                .toList();
        List<InventoryEntity> inventoryLists = inventoryRepo.findAllInventoryCandidates(productIds);

        return Map.of();
    }

}
