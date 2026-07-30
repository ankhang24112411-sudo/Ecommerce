package com.khang.backendecommerce.domain.delivery.service.impl;

import com.khang.backendecommerce.domain.delivery.service.DeliveryService;
import com.khang.backendecommerce.domain.inventory.entity.InventoryEntity;
import com.khang.backendecommerce.domain.inventory.service.InventoryService;
import com.khang.backendecommerce.domain.product.entity.ProductEntity;
import com.khang.backendecommerce.domain.user.entity.UserEntity;
import com.khang.backendecommerce.domain.warehouse.entity.WarehouseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final InventoryService inventoryService;
    private final DeliveryRouteRepository deliveryRouteRepo;
    @Override
    public BigDecimal calculateProductDeliveryAmount(UserEntity user, InventoryEntity inventory) {
        String userStateId = user.getState().getId();
        String warehouseStateId = inventory.getWarehouse().getState().getId();

        return BigDecimal;
    }
}
