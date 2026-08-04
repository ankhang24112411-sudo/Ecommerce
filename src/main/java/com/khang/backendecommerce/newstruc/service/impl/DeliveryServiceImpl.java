package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.newstruc.entity.CartEntity;
import com.khang.backendecommerce.newstruc.entity.CartItemEntity;
import com.khang.backendecommerce.newstruc.entity.DeliveryFeeEntity;
import com.khang.backendecommerce.newstruc.entity.DeliveryRouteEntity;
import com.khang.backendecommerce.newstruc.repo.DeliveryFeeRepository;
import com.khang.backendecommerce.newstruc.repo.DeliveryRouteRepository;
import com.khang.backendecommerce.newstruc.service.DeliveryService;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;
import com.khang.backendecommerce.newstruc.repo.InventoryRepository;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import com.khang.backendecommerce.newstruc.entity.UserEntity;
import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehousesId(Set<String> warehouseIds,String userStateId) {
        List<DeliveryFeeEntity> findAllDeliveriesFee = deliveryFeeRepo.findAllForCheckOut(warehouseIds, userStateId);


        return findAllDeliveriesFee.stream()
                .collect(Collectors.toMap(deliveryFeeEntity ->
                        deliveryFeeEntity.getDeliveryRoute().getStateFrom().getId(),
                        Function.identity()));
    }

    @Override
    public BigDecimal calculateDeliveryFee(InventoryEntity inventory,
                                           String userStateId,
                                           Map<String, DeliveryFeeEntity> deliveryFeeByWarehouseState) {
     String wareHouseStateId = inventory.getWarehouse().getState().getId();
     if(wareHouseStateId.equals(userStateId)){
         return BigDecimal.ZERO;
     }
     DeliveryFeeEntity deliveryFee = deliveryFeeByWarehouseState.get(wareHouseStateId);
     if(!deliveryFeeByWarehouseState.containsKey(wareHouseStateId)){
         throw ApplicationErrors.DELIVERY_ROUTE_NOT_FOUND;
     }
        return deliveryFee.getBaseFee();
    }

//    @Override
//    public Map<DeliveryRouteEntity, DeliveryFeeEntity> findDeliveryFeeOnRoute(List<InventoryEntity> inventories, String userStateId) {
//        return Map.of();
//    }
}
