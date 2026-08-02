package com.khang.backendecommerce.newstruc.domain.order.checkout.registry;

import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import com.khang.backendecommerce.newstruc.entity.StoreEntity;

import java.math.BigDecimal;

public record CheckoutItemSnapshot(
  ProductEntity product,
  int quantity,
  StoreEntity store
) { }