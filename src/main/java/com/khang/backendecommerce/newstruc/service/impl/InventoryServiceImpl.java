package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;
import com.khang.backendecommerce.newstruc.repo.InventoryRepository;
import com.khang.backendecommerce.newstruc.service.InventoryService;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepo;



    public void checkProductQuantityUpdate(int quantityUpdate, int inventoryQuantity ) {

    }
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
}
