package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.newstruc.entity.CartEntity;
import com.khang.backendecommerce.newstruc.entity.CartItemEntity;
import com.khang.backendecommerce.newstruc.entity.DeliveryFeeEntity;
import com.khang.backendecommerce.newstruc.entity.DeliveryRouteEntity;
import com.khang.backendecommerce.newstruc.repo.DeliveryFeeRepository;
import com.khang.backendecommerce.newstruc.repo.DeliveryRouteRepository;
import com.khang.backendecommerce.newstruc.repo.DeliveryService;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;
import com.khang.backendecommerce.newstruc.repo.InventoryRepository;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import com.khang.backendecommerce.newstruc.domain.user.entity.UserEntity;
import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "DELIVERY - SERVICE")
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRouteRepository deliveryRouteRepo;
    private final DeliveryFeeRepository deliveryFeeRepo;
    private final InventoryRepository inventoryRepo;
//TODO code chua toi uu , N + 1
    @Override
    public BigDecimal calculateProductDeliveryAmount(UserEntity user, InventoryEntity inventory) {


        String userStateId = user.getState().getId();
        String warehouseStateId = inventory.getWarehouse().getState().getId();
        if(userStateId.equals(warehouseStateId)){
            return BigDecimal.ZERO;
        }
        log.info("warehouse state id :{}  " , warehouseStateId );
        log.info("User state id :{} ", userStateId );


        DeliveryRouteEntity route = deliveryRouteRepo.findByStateFrom_IdAndStateTo_Id(warehouseStateId, userStateId)
                .orElseThrow(() -> ApplicationErrors.DELIVERY_ROUTE_NOT_FOUND);
        log.info("Delivery {} route is from {} to {}" ,route.getId(), route.getStateFromName() ,route.getStateToName());


        DeliveryFeeEntity deliveryFee =deliveryFeeRepo.findByDeliveryRoute_Id(route.getId())
                .orElseThrow(() ->  ApplicationErrors.INVALID_DELIVERY_FEE);
        log.info("Delivery fee is : {} and company {}" , deliveryFee.getDeliveryRoute().getId() , deliveryFee.getCompanyId());

        return deliveryFee.getBaseFee();
    }
    @Override
    public BigDecimal calculateCartDeliveryAmount(UserEntity user ,CartEntity cart){
        List<String> productIds = cart.getCartItemList().stream()
                .map(CartItemEntity::getProduct)
                .map(ProductEntity::getId).toList();
        List<InventoryEntity> inventories = inventoryRepo.findAllByProduct_IdIn( productIds);
        return inventories.stream()
                .map( inventoryEntity -> calculateProductDeliveryAmount(user, inventoryEntity))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }
}
