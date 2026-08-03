package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.CartEntity;
import com.khang.backendecommerce.newstruc.entity.DeliveryFeeEntity;
import com.khang.backendecommerce.newstruc.entity.DeliveryRouteEntity;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;
import com.khang.backendecommerce.newstruc.domain.user.entity.UserEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DeliveryService {
    BigDecimal calculateProductDeliveryAmount(UserEntity user, InventoryEntity inventory);
     BigDecimal calculateCartDeliveryAmount(UserEntity user , CartEntity cart);
//  Map<DeliveryRouteEntity, DeliveryFeeEntity>  findDeliveryFeeOnRoute(List<InventoryEntity> inventories, String userStateId);
    Map<String , DeliveryFeeEntity> deliveryFeeEntityByWarehousesId(Set<String> warehouseIds, String userStateId);
     BigDecimal calculateDeliveryFee(InventoryEntity inventory, String recipientStateId, Map<String, DeliveryFeeEntity> deliveryFeeByWarehouseState);
    }
