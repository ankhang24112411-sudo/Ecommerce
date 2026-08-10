package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.newstruc.entity.OrderEntity;
import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;
import com.khang.backendecommerce.newstruc.entity.UserEntity;

import java.math.BigDecimal;

public interface RefundService {
  void handleRefundWhenSubOrderReject(SubOrderEntity subOrder, OrderEntity order, UserEntity user, BigDecimal refundAmount);
}
