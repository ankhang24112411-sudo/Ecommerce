package com.khang.backendecommerce.domain.delivery.service.impl;

import com.khang.backendecommerce.domain.cart.entity.CartEntity;
import com.khang.backendecommerce.domain.cart.entity.CartItemEntity;
import com.khang.backendecommerce.domain.delivery.entity.DeliveryFeeEntity;
import com.khang.backendecommerce.domain.delivery.entity.DeliveryRouteEntity;
import com.khang.backendecommerce.domain.delivery.repository.DeliveryFeeRepository;
import com.khang.backendecommerce.domain.delivery.repository.DeliveryRouteRepository;
import com.khang.backendecommerce.domain.delivery.service.DeliveryService;
import com.khang.backendecommerce.domain.inventory.entity.InventoryEntity;
import com.khang.backendecommerce.domain.inventory.repo.InventoryRepository;
import com.khang.backendecommerce.domain.inventory.service.InventoryService;
import com.khang.backendecommerce.domain.product.entity.ProductEntity;
import com.khang.backendecommerce.domain.user.entity.UserEntity;
import com.khang.backendecommerce.domain.warehouse.entity.WarehouseEntity;
import com.khang.backendecommerce.infrastructure.common.enums.ErrorCode;
import com.khang.backendecommerce.infrastructure.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final InventoryService inventoryService;
    private final DeliveryRouteRepository deliveryRouteRepo;
    private final DeliveryFeeRepository deliveryFeeRepo;
    private final InventoryRepository inventoryRepo;
    private final DeliveryService deliveryService;

    @Override
    public BigDecimal calculateProductDeliveryAmount(UserEntity user, InventoryEntity inventory) {
        String userStateId = user.getState().getId();
        String warehouseStateId = inventory.getWarehouse().getState().getId();
        DeliveryRouteEntity route = deliveryRouteRepo.findByStateFrom_IdAndStateTo_Id(warehouseStateId, userStateId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_ROUTE_NOT_FOUND));

        DeliveryFeeEntity deliveryFee =deliveryRouteRepo.findByDeliveryRouteId(route.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_FEE_NOT_FOUND));
        return deliveryFee.getBaseFee();
    }
    @Override
    public BigDecimal calculateCartDeliveryAmount(UserEntity user ,CartEntity cart){
        List<String> productIds = cart.getCartItemList().stream()
                .map(CartItemEntity::getProduct)
                .map(ProductEntity::getId).toList();
        List<InventoryEntity> inventories = inventoryRepo.findAllByProduct_IdIn( productIds);
        return inventories.stream()
                .map( inventoryEntity -> deliveryService.calculateProductDeliveryAmount(user, inventoryEntity))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }
}
