package com.khang.backendecommerce.domain.delivery.service;

import com.khang.backendecommerce.domain.inventory.entity.InventoryEntity;
import com.khang.backendecommerce.domain.product.entity.ProductEntity;
import com.khang.backendecommerce.domain.user.entity.UserEntity;

import java.math.BigDecimal;

public interface DeliveryService {
    BigDecimal calculateProductDeliveryAmount(UserEntity user, InventoryEntity inventory);
}
