package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.infrastructure.common.enums.InventoryStatus;
import com.khang.backendecommerce.infrastructure.configuration.CurrentUserProvider;
import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.newstruc.dto.response.InventoryNewCartContext;
import com.khang.backendecommerce.newstruc.dto.response.store.CreateProductRequest;
import com.khang.backendecommerce.newstruc.dto.response.store.ProductResponse;
import com.khang.backendecommerce.newstruc.entity.*;
import com.khang.backendecommerce.newstruc.repo.*;
import com.khang.backendecommerce.newstruc.service.DeliveryService;
import com.khang.backendecommerce.newstruc.service.InventoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "INVENTORY - SERVICE")
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepo;
    private final DeliveryService deliveryService;
    private final DeliveryFeeRepository deliveryFeeRepo;
    private final CurrentUserProvider currentUserProvider;
    private final StoreRepository storeRepo;
    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;
    private final WarehouseRepository warehouseRepo;
    private final ProductImageRepository productImageRepo;

    //    public void checkProductQuantityUpdate(int quantityUpdate, int inventoryQuantity ) {
//
//    }
    public InventoryEntity checkProductExistingInventory(String productId) {
        return inventoryRepo.findByProduct_Id(productId).orElseThrow(() -> ApplicationErrors.PRODUCT_EXISTED);

    }

    @Override
    public InventoryNewCartContext findProductAvailability(ProductEntity product, int quantity, UserEntity user) {
        List<InventoryEntity> inventoryList = inventoryRepo.findAllInventoryCandidatesWithEnoughStock(product.getId(), quantity);
        if (inventoryList.isEmpty()) {
            throw ApplicationErrors.INVENTORY_NOT_ENOUGH;
        }
        Map<String, List<InventoryEntity>> wareHouseStateIdByInventory = inventoryList.stream()
                .collect(Collectors.groupingBy(inventory -> inventory.getWarehouse().getState().getId()));

        List<DeliveryFeeEntity> deliveryFeeEntities = deliveryFeeRepo.findAllForCheckOut(wareHouseStateIdByInventory.keySet(), user.getState().getId());
        DeliveryFeeEntity cheapest = deliveryFeeEntities.stream()
                .collect(Collectors.toMap(deliveryFeeEntity -> deliveryFeeEntity.getDeliveryRoute().getStateFrom().getId(), Function.identity()))
                .values()
                .stream()
                .min(Comparator.comparing(DeliveryFeeEntity::getBaseFee)).orElseThrow(() -> ApplicationErrors.DELIVERY_FEE_NOT_FOUND);

        InventoryEntity bestInventory = inventoryList.stream()
                .filter(inventory -> inventory.getWarehouse().getState().getId().equals(cheapest.getDeliveryRoute().getStateFrom().getId()))
                .max(Comparator.comparing(inventory -> inventory.getAvailableQuantity() - inventory.getReservedQuantity()))
                .orElseThrow(() -> ApplicationErrors.INVENTORY_NOT_FOUND);
        InventoryNewCartContext context = new InventoryNewCartContext(bestInventory, cheapest);
        return context;
    }

    public Map<String, List<InventoryEntity>> findOptimizeInventory(CartEntity cart, ProductEntity product, int quantity,
                                                                    Map<String, List<InventoryEntity>> inventoriesByProductId) {
        List<InventoryEntity> inventoryList = inventoryRepo.findAllInventoryCandidatesWithEnoughStock(product.getId(), quantity);

        if (!inventoriesByProductId.containsKey(product.getId())) {
            inventoriesByProductId.put(product.getId(), inventoryList);
        }
        return inventoriesByProductId;
    }

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        UserEntity user = currentUserProvider.getCurrentUser();
        if (!storeRepo.existsByOwner_Id(user.getId())) {
            throw ApplicationErrors.ACCESS_DENIED;
        }
        StoreEntity store = storeRepo.findByOwner_Id(user.getId())
                .orElseThrow(() -> ApplicationErrors.ACCESS_DENIED);

        CategoryEntity category = categoryRepo.findById(request.categoryId())
                .orElseThrow(() -> ApplicationErrors.CATEGORY_NOT_FOUND);

        ProductEntity product = ProductEntity.builder()
                .name(request.name())
                .sku(request.sku())
                .price(request.price())
                .description(request.description())
                .store(store)
                .category(category)
                .build();

        productRepo.save(product);

        List<InventoryEntity> inventories = request.inventories().stream()
                .map(inventrequest -> {

                    WarehouseEntity warehouse = warehouseRepo.findById(inventrequest.warehouseId()).orElseThrow(() -> ApplicationErrors.WAREHOUSE_NOT_FOUND);

                    return InventoryEntity.builder()
                            .product(product)
                            .sku(product.getSku())
                            .description(product.getDescription())
                            .warehouse(warehouse)
                            .availableQuantity(inventrequest.quantity())
                            .reservedQuantity(0)
                            .inventoryStatus(InventoryStatus.IN_STOCK).build();
                }).collect(Collectors.toList());

        inventoryRepo.saveAll(inventories);
        List<ProductImageEntity> productImages = request.images()
                .stream()
                .map(productImageRequest -> {
                    return ProductImageEntity.builder()
                            .product(product).image(productImageRequest.image()).primary(productImageRequest.primary()).displayOrder(productImageRequest.displayOrder()).build();
                }).collect(Collectors.toList());
        productImageRepo.saveAll(productImages);
        return ProductResponse.builder()
                .id(product.getId()).name(product.getName()).price(product.getPrice()).build()
                ;
    }


    @Override
    public Map<String, List<InventoryEntity>> loadAndLockInventories(List<CartItemEntity> cartItemList) {
        List<String> productIds = cartItemList.stream()
                .map(item -> item.getProduct().getId())
                .toList();
        List<InventoryEntity> inventoryLists = inventoryRepo.findAllInventoryCandidates(productIds)

                .stream()
                .filter(inventory -> inventory.getAvailableQuantity() > 0)

                .toList();
        Map<String, List<InventoryEntity>> inventoryByProduct = inventoryLists.stream()
                .collect(Collectors.groupingBy(inventory -> inventory.getProduct().getId()));

        return inventoryByProduct;
    }

    @Override
    public Set<String> extractWarehouseStateIds(Map<String, List<InventoryEntity>> productByInventories) {
        return productByInventories.values().stream()
                .flatMap(Collection::stream)
                .map(inventory -> inventory.getWarehouse().getState().getId())
                .collect(Collectors.toSet());
    }

    @Override
    public InventoryEntity selectInventory(ProductEntity product,
                                           int productQuantity,
                                           List<InventoryEntity> inventoryEntities,
                                           Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehouseStateId,
                                           String userStateId) {
        if (inventoryEntities.isEmpty()) {
            throw ApplicationErrors.INVENTORY_NOT_FOUND;
        }
        List<InventoryEntity> candidates = inventoryEntities.stream()
                .filter(inventory -> canDeliveryFromWarehouse(
                        inventory, deliveryFeeEntityByWarehouseStateId, userStateId
                )).toList();

        InventoryEntity selectInventory = candidates.stream()
                .peek(inventory -> log.info("Candidate inventory={}stock={} reserved={} requested={}", inventory.getId(), inventory.getAvailableQuantity(), inventory.getReservedQuantity(), productQuantity))
                .filter(inventory -> inventory.getAvailableQuantity() - inventory.getReservedQuantity() >= productQuantity)
                .sorted(Comparator.comparing(
                                (InventoryEntity inventory) ->
                                        deliveryService.calculateDeliveryFee(inventory, userStateId, deliveryFeeEntityByWarehouseStateId))
                        .thenComparing(InventoryEntity::getAvailableQuantity))
                .findFirst().orElseThrow(() -> ApplicationErrors.INVENTORY_NOT_ENOUGH);
        log.info(
                "Check stock productId= {} inventoryId= {}stock= {} reserved= {} requested= {}", product.getId(), selectInventory.getId(), selectInventory.getAvailableQuantity(), selectInventory.getReservedQuantity(), productQuantity
        );
        if (selectInventory.getAvailableQuantity() - selectInventory.getReservedQuantity() < productQuantity) {
            throw ApplicationErrors.INVENTORY_NOT_ENOUGH;
        }

        int totalSumProduct = inventoryEntities.stream()
                .mapToInt(InventoryEntity::getAvailableQuantity)
                .sum();
        if (totalSumProduct < productQuantity) {
            throw ApplicationErrors.SINGLE_INVENTORY_NOT_ENOUGH_STOCK;
        }
        return selectInventory;
    }

    @Override
    public Map<String, List<InventoryEntity>> loadInventories(List<CartItemEntity> cartItemList) {
        List<String> productIds = cartItemList.stream()
                .map(item -> item.getProduct().getId())
                .toList();
        List<InventoryEntity> inventoryLists = inventoryRepo.findAllInventoryCandidatesWithoutLock(productIds)
                .stream()
                .filter(inventory -> inventory.getAvailableQuantity() > 0)
                .toList();
        Map<String, List<InventoryEntity>> inventoryByProduct = inventoryLists.stream()
                .collect(Collectors.groupingBy(inventory -> inventory.getProduct().getId()));

        return inventoryByProduct;
    }

    public boolean canDeliveryFromWarehouse(InventoryEntity inventory,
                                            Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehouseStateId,
                                            String userStateId
    ) {
        String wareHouseStateId = inventory.getWarehouse().getState().getId();
        if (wareHouseStateId.equals(userStateId)) {
            return true;
        }
        return deliveryFeeEntityByWarehouseStateId.containsKey(wareHouseStateId);
    }
}
