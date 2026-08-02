package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.CartEntity;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;
import com.khang.backendecommerce.newstruc.domain.user.entity.UserEntity;

import java.math.BigDecimal;

public interface DeliveryService {
    BigDecimal calculateProductDeliveryAmount(UserEntity user, InventoryEntity inventory);
     BigDecimal calculateCartDeliveryAmount(UserEntity user , CartEntity cart);

    }
