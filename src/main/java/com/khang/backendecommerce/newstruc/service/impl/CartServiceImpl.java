package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.newstruc.domain.order.dto.AllocatedItem;
import com.khang.backendecommerce.newstruc.dto.request.CartItemPriceResponse;
import com.khang.backendecommerce.newstruc.dto.response.CartItemResponse;
import com.khang.backendecommerce.newstruc.dto.response.InventoryNewCartContext;
import com.khang.backendecommerce.newstruc.dto.response.SubOrderSummaryResponse;
import com.khang.backendecommerce.newstruc.entity.*;
import com.khang.backendecommerce.newstruc.projection.CartMapper;
import com.khang.backendecommerce.newstruc.repo.CartItemRepository;
import com.khang.backendecommerce.newstruc.repo.CartRepository;
import com.khang.backendecommerce.newstruc.repo.DeliveryRouteRepository;
import com.khang.backendecommerce.newstruc.repo.ProductRepository;
import com.khang.backendecommerce.newstruc.service.CartService;
import com.khang.backendecommerce.newstruc.service.DeliveryService;
import com.khang.backendecommerce.infrastructure.discountinfra.DiscountContext;
import com.khang.backendecommerce.newstruc.service.DiscountService;
import com.khang.backendecommerce.newstruc.service.InventoryService;
import com.khang.backendecommerce.newstruc.dto.response.OrderSummaryResponse;
import com.khang.backendecommerce.newstruc.service.ProductService;
import com.khang.backendecommerce.infrastructure.common.enums.InventoryStatus;
import com.khang.backendecommerce.infrastructure.configuration.CurrentUserProvider;

import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.infrastructure.util.AppConst;
import com.khang.backendecommerce.infrastructure.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CART - SERVICE")
public class CartServiceImpl implements CartService {
    private final CurrentUserProvider currentUserProvider;
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final CartMapper cartMapper;
    private final InventoryService inventoryService;
    private final ProductService productService;
    private final DeliveryService deliveryService;
    private final DiscountService discountService;
    private final ProductRepository productRepo;
    private final DeliveryRouteRepository deliveryRouteRepo;
    private Map<ProductEntity,BigDecimal> priceByProduct(List<CartItemEntity> cartItemList){
        return cartItemList.stream()
                .map(CartItemEntity::getProduct)
                .collect(Collectors.toMap(product -> product, ProductEntity::getPrice));
    }
    @Override
    public List<CartItemResponse> getAllCartItems() {
        CartEntity cart = findByUserId(currentUserProvider.getCurrentUserId());
         List<CartItemEntity> cartItemList = cartItemRepo.findAllCartItemInGetAllWithProduct(cart.getId());
         Map<ProductEntity,BigDecimal> priceByProduct = priceByProduct(cartItemList);

//         cartItemList.forEach(cartItem -> cartItem.setInventoryStatus(getInventoryStatus(cartItem)));
        List<CartItemResponse> cartItemResponses =  cartItemList.stream().map(cartItem -> {
            ProductEntity product = cartItem.getProduct();
            BigDecimal unitPrice = priceByProduct.get(product);
             return    CartItemResponse.builder()
                    .name(cartItem.getProduct().getName())
                    .quantity(cartItem.getQuantity())
                    .unitPrice(unitPrice)
                    .inventoryStatus(cartItem.getInventoryStatus())
                    .build();
        }).toList();
        return  cartItemResponses;
    }
//    public InventoryStatus getInventoryStatus(CartItemEntity cartItem){
//        return cartItem.getInventory().getInventoryStatus();
//    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CartItemPriceResponse updateCartItemQuantity(String cartItemId, int quantityUpdate) {
        UserEntity user = currentUserProvider.getCurrentUser();
        CartEntity cart = findByUserId(user.getId());

        final CartItemEntity cartItem = checkCartItemFromUser(cartItemId, cart, user);
        checkNullQuantityCartItem(cartItem, quantityUpdate);
        final ProductEntity product = cartItem.getProduct();
        productService.isProductActive(product);

        final InventoryEntity inventory = inventoryService.checkProductExistingInventory(product.getId());
        if ((quantityUpdate > 0 && inventory.getAvailableQuantity() - inventory.getReservedQuantity() - quantityUpdate < 0) || inventory.getInventoryStatus() == InventoryStatus.OUT_OF_STOCK) {
            throw ApplicationErrors.INVENTORY_NOT_ENOUGH;
        }
        if(quantityUpdate < 0 &&( cartItem.getQuantity() + quantityUpdate <= 0 )){
            cart.removeCartItem(cartItem);
        }
        final int newQuantity = cartItem.getQuantity() + quantityUpdate;

        cartItem.setQuantity(newQuantity);
        cartItemRepo.save(cartItem);


        cartRepo.save(cart);

        return cartMapper.toCartItemPriceResponse(cartItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteCartItems(String itemId) {
        UserEntity user = currentUserProvider.getCurrentUser();
        CartEntity cart = findByUserId(user.getId());
        if(cart == null ){
            throw ApplicationErrors.CART_NOT_FOUND;
        }
       CartItemEntity cartItem = checkCartItemFromUser(itemId, cart, user);
        cart.removeCartItem(cartItem);
//        calculateCartSubTotalAndSet(cart);

        return "Delete CartItem successful";
    }
    public BigDecimal calculateCartSubTotalAndSet(CartEntity cart){
        List<CartItemEntity> cartItemList = cartItemRepo.findAllCartItemInGetAllWithProduct(cart.getId());
        Map<ProductEntity,BigDecimal> priceByProduct = cartItemList.stream().map(CartItemEntity::getProduct)
                .collect(Collectors.toMap(productEntity -> productEntity,ProductEntity::getPrice));

        return cart.getCartItemList().stream()
                .map(cartItemEntity -> {
                    ProductEntity product = cartItemEntity.getProduct();
                    BigDecimal price = priceByProduct.get(product);
                    return  price;
                }).reduce(BigDecimal.ZERO,BigDecimal::add);

    }
    @Override
    public OrderSummaryResponse createBuyNow(UserEntity user, String discountName, String productId) {
        ProductEntity product = productRepo.findProductAndShopByProductId(productId).orElseThrow(() -> ApplicationErrors.PRODUCT_NOT_FOUND);
        productService.isProductActive(product);
        CartEntity cart = cartRepo.findCartAndCartItemsByUserId(user.getId());
        if (cart == null) {
            cart = createNewCart(user, product, AppConst.BUY_NOW_QUANTITY);
            return getOrderSummaryResponseForBuyNow(user,discountName,cart);
        }
           return getOrderSummaryResponse(user, cart , product,AppConst.BUY_NOW_QUANTITY);


    }

    public OrderSummaryResponse getOrderSummaryResponse(UserEntity user  ,CartEntity cart, ProductEntity product, int quantity) {
        List<CartItemEntity> cartItemList = cart.getCartItemList();
        DiscountCustomerEntity discountCustomer = cart.getDiscount();
        DiscountEntity discount = discountCustomer == null ? null : discountService.findAndCheckDiscountCustomer(discountCustomer);
        Map<String, List<InventoryEntity>> inventoriesByProductIdOld = inventoryService.loadInventories(cartItemList);
        Map<String, List<InventoryEntity>> inventoriesByProductId ;
       CartItemEntity newCartItem = null;
        if(product != null ) {
            inventoriesByProductId = inventoryService.findOptimizeInventory(cart, product, quantity, inventoriesByProductIdOld);
             newCartItem = CartItemEntity.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
//                     .subtotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                    .build();
            cart.addCartItem(newCartItem);
        }else{
            inventoriesByProductId = inventoriesByProductIdOld;
        }

      Set<String>  wareHouseStateIds = inventoryService.extractWarehouseStateIds(inventoriesByProductId);
        Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehouseIds = deliveryService.deliveryFeeEntityByWarehousesStateId(wareHouseStateIds, user.getState().getId());
        List<AllocatedItem> allocatedItemList = findAllocate(cart ,cartItemList, inventoriesByProductId,wareHouseStateIds,deliveryFeeEntityByWarehouseIds , user.getState().getId(), newCartItem);

        Map<SubOrderGroupKey , List<AllocatedItem>> groupedItems = allocatedItemList.stream()
                .collect(Collectors
                        .groupingBy(item ->
                                new SubOrderGroupKey(item.product().getStore().getId(),
                                        item.inventory().getWarehouse().getId(), item.deliveryRoute().getId())));

        BigDecimal shippingFee = groupedItems.values().stream()
                .map(item -> item.get(0).deliveryFee())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal subtotal = groupedItems.values().stream()
                .flatMap(Collection::stream)
                .map(AllocatedItem::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discountAmount = BigDecimal.ZERO;
        if(discount != null) {
            DiscountContext context = DiscountContext.builder()
                    .subtotal(subtotal)
                    .deliveryAmount(shippingFee)
                    .build();
            discountAmount = discountService.calculateDiscount(discountCustomer,context);
        }
        return OrderSummaryResponse.builder()
                .subtotal(subtotal)
                .discountAmount(discountAmount)
                .deliveryAmount(shippingFee)
                .totalAmount(subtotal.add(shippingFee).subtract(discountAmount))
                .build();
//        OrderSummaryResponse orderSummaryResponse = groupedItems.values().stream()
//                .map( orderItems ->{
//                    AllocatedItem firstItem = orderItems.get(0);
//                    SubOrderSummaryResponse subOrderSummaryResponses =SubOrderSummaryResponse.builder()
//                            .deliveryFee(firstItem.deliveryFee())
//                            .build();
//                    BigDecimal subtotal = orderItems.stream()
//                            .map(AllocatedItem::subtotal)
//                            .reduce(BigDecimal.ZERO,BigDecimal::add);
//                    BigDecimal deliveryFee =
////                    OrderSummaryResponse orderSummaryResponse1 = OrderSummaryResponse.builder().build();
//                }


//        OrderSummaryResponse orderSummaryResponse = allocatedItemList.stream()
//                .map( allocatedItem -> {
//                    allocatedItemList.stream().
//                    OrderSummaryResponse orderSummaryResponse1 = OrderSummaryResponse.builder().build();
//                })
//
//        log.info("add Product to cart : {}" ,cart.getId());
//        InventoryEntity inventory = inventoryService.findProductAvailability(product, quantity,user  );
//        BigDecimal subtotalNewCartItem = product.getPrice().multiply(BigDecimal.valueOf(quantity));
//
//        CartItemEntity cartItem= CartItemEntity.builder()
//                .cart(cart)
//                .product(product)
//                .quantity(AppConst.BUY_NOW_QUANTITY)
//                .subtotal(subtotalNewCartItem)
//                .inventoryStatus(inventory.getInventoryStatus())
//                .build();
//        log.info("add  cartItem success with cart id : {}" , cartItem.getCart());
//        cart.addCartItem(cartItem);
//
//        BigDecimal oldCartSubtotal = cart.getSubtotal();
//        BigDecimal newCartSubtotal = oldCartSubtotal.add(subtotalNewCartItem);
//        cart.setSubtotal(newCartSubtotal);
//        cart.setTotalAmount(newCartSubtotal);
    }

    private List<AllocatedItem> findAllocate(CartEntity cart, List<CartItemEntity> cartItemList,
                                             Map<String, List<InventoryEntity>> inventoriesByProductId,
                                             Set<String> wareHouseStateIds,
                                             Map<String, DeliveryFeeEntity> deliveryFeeEntityByWarehouseIds,
                                             String userStateId,
                                             CartItemEntity newCartItem) {
        List<AllocatedItem> result = new ArrayList<>();
        for(CartItemEntity cartItem : cartItemList){
            ProductEntity product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();
            List<InventoryEntity> inventoryList = inventoriesByProductId.get(product.getId());
            InventoryEntity selectedInventory = inventoryService.selectInventory(product,quantity,inventoryList, deliveryFeeEntityByWarehouseIds, userStateId);
          if(selectedInventory == null){
              throw ApplicationErrors.INVENTORY_NOT_FOUND;
          }
          DeliveryRouteEntity deliveryRoute = deliveryRouteRepo.findByStateFrom_IdAndStateTo_Id(selectedInventory.getWarehouse().getState().getId() , userStateId).orElseThrow(() -> ApplicationErrors.DELIVERY_ROUTE_NOT_FOUND);
          BigDecimal fee = deliveryService.calculateDeliveryFee(selectedInventory, userStateId, deliveryFeeEntityByWarehouseIds);
          BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
//          if(cartItem.getId().equals(newCartItem.getId())){
//              cartItem.setSubtotal(subtotal);
//          }
          AllocatedItem allocatedItem = AllocatedItem.builder()
                  .cartItem(cartItem)
                  .product(product)
                  .inventory(selectedInventory)
                  .quantity(quantity)
                  .unitPrice(product.getPrice())
                  .subtotal(subtotal)
                  .deliveryRoute(deliveryRoute)
                  .deliveryFee(fee)
                  .build();
          result.add(allocatedItem);
        }
        return result;
    }


    @Override
    public OrderSummaryResponse getOrderSummaryResponseForBuyNow(UserEntity user, String discountName , CartEntity cart ) {
        BigDecimal subTotal = calculateCartSubTotalAndSet( cart);
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal deliveryAmount = deliveryService.calculateCartDeliveryAmount(user , cart);
        if(discountName == null){
            totalAmount = subTotal.add(deliveryAmount);
            return   OrderSummaryResponse.builder()
                    .subtotal(subTotal)
                    .discountAmount(null)
                    .deliveryAmount(deliveryAmount)
                    .totalAmount(totalAmount)
                    .build();
        }
        DiscountCustomerEntity discount = discountService.checkDiscountValidationFromUser(user.getId(), discountName);
        boolean sameDiscount = discountService.isSameDiscount(cart,discountName);
        if(!sameDiscount){
           cart.setDiscount(discount);
        }
        DiscountContext context = DiscountContext.builder()
                .subtotal(subTotal)
                .deliveryAmount(deliveryAmount)
                .build();
        discountAmount = discountService.calculateDiscount(discount , context);
        totalAmount = subTotal.add(deliveryAmount).subtract(discountAmount);
         return  OrderSummaryResponse.builder()
                    .subtotal(subTotal)
                    .discountAmount(discountAmount)
                    .deliveryAmount(deliveryAmount)
                    .totalAmount(totalAmount)
                    .build();
    }

    @Override
    public CartEntity findByUserId(String id) {
        return cartRepo.findByUser_Id(id);
    }


    @Override
    public List<CartItemEntity> loadCartItems(CartEntity cart, UserEntity user) {
        List<CartItemEntity> cartItems = cartItemRepo.findAllForCheckout(cart.getId());
        log.debug(
                "items loaded: cartId={}, count={}, itemIds={}",
                cart.getId(),
                cartItems.size(),
                cartItems.stream()
                        .map(CartItemEntity::getId)
                        .toList()
        );
        cartItems.forEach(cartItem -> {
            log.debug("Checking cart ownership: cartItemId={}, cartId={}, userId={}", cartItem.getId(), cart.getId(), user.getId());
            checkCartItemFromUser(cartItem.getId(), cart, user);
            log.debug("Checking product status: cartItemId={}, productId={}, quantity={}", cartItem.getId(), cartItem.getProduct().getId(), cartItem.getQuantity());
            productService.isProductActive(cartItem.getProduct());
            log.debug("Cart item validation passed: cartItemId={}", cartItem.getId());
        });

        log.info(
                "Checkout cart validated successfully: cartId={}, userId={}, itemCount={}",
                cart.getId(),
                user.getId(),
                cartItems.size()
        );

        return cartItems;
    }


    private CartEntity createNewCart(UserEntity user,ProductEntity  product, int quantity) {
        InventoryNewCartContext inventoryContext = inventoryService.findProductAvailability(product, quantity, user );
        InventoryEntity inventory = inventoryContext.inventory();
//        DeliveryFeeEntity deliveryFee = inventoryContext.deliveryFee();
//        BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));

      CartEntity cart =  CartEntity.builder()
                .user(user)
//                .totalAmount(subtotal)
//                .deliveryFee(deliveryFee.getBaseFee())
                .cartItemList(new ArrayList<>())
                .build();
      log.info("New cart created : {} from User {}" , cart.getId() , cart.getUser().getId());
      CartItemEntity itemEntity = CartItemEntity.builder()
              .cart(cart)
              .product(product)
              .quantity(quantity)
              .inventoryStatus(inventory.getInventoryStatus())
//              .inventory(inventory)
//              .deliveryFee(deliveryFee.getBaseFee())
              .build();
//      cart.setTotalAmount(subtotal.add(itemEntity.getDeliveryFee()));
      cart.addCartItem(itemEntity);
      cartRepo.save(cart);
        return cart;
    }


    private void checkNullQuantityCartItem(CartItemEntity cartItem, int quantityUpdate){
        if(quantityUpdate < 0 && cartItem.getQuantity() - quantityUpdate <= 0){
            cartItemRepo.delete(cartItem);
        }
    }

    private CartItemEntity checkCartItemFromUser(String cartItemId,CartEntity cart,UserEntity user){
        ValidationUtils.throwIf(cartItemRepo.existsByIdAndCart_Id(cartItemId, cart.getId()),() -> ApplicationErrors.CART_ITEM_NOT_FOUND );
        String userId = user.getId();
        return cartItemRepo.findByIdAndCart_User_Id(cartItemId , userId).
                orElseThrow(() ->  ApplicationErrors.CART_ITEM_NOT_FOUND);
    }

}
