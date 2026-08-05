package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.newstruc.entity.CartEntity;
import com.khang.backendecommerce.newstruc.entity.DeliveryFeeEntity;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;
import com.khang.backendecommerce.newstruc.entity.UserEntity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public interface DeliveryService {
    BigDecimal calculateProductDeliveryAmount(UserEntity user, InventoryEntity inventory);
     BigDecimal calculateCartDeliveryAmount(UserEntity user , CartEntity cart);
//  Map<DeliveryRouteEntity, DeliveryFeeEntity>  findDeliveryFeeOnRoute(List<InventoryEntity> inventories, String userStateId);
    Map<String , DeliveryFeeEntity> deliveryFeeEntityByWarehousesStateId(Set<String> warehouseIds, String userStateId);
     BigDecimal calculateDeliveryFee(InventoryEntity inventory, String recipientStateId, Map<String, DeliveryFeeEntity> deliveryFeeByWarehouseState);
    }
