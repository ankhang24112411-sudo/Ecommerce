package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.newstruc.dto.request.CartItemPriceResponse;
import com.khang.backendecommerce.newstruc.dto.response.CartItemResponse;
import com.khang.backendecommerce.newstruc.entity.CartEntity;
import com.khang.backendecommerce.newstruc.entity.CartItemEntity;
import com.khang.backendecommerce.newstruc.projection.CartMapper;
import com.khang.backendecommerce.newstruc.repo.CartItemRepository;
import com.khang.backendecommerce.newstruc.repo.CartRepository;
import com.khang.backendecommerce.newstruc.service.CartService;
import com.khang.backendecommerce.newstruc.service.DeliveryService;
import com.khang.backendecommerce.infrastructure.discountinfra.DiscountContext;
import com.khang.backendecommerce.newstruc.entity.DiscountCustomerEntity;
import com.khang.backendecommerce.newstruc.service.DiscountService;
import com.khang.backendecommerce.newstruc.entity.InventoryEntity;
import com.khang.backendecommerce.newstruc.service.InventoryService;
import com.khang.backendecommerce.newstruc.dto.response.OrderSummaryResponse;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import com.khang.backendecommerce.newstruc.service.ProductService;
import com.khang.backendecommerce.newstruc.entity.UserEntity;
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
    @Override
    public List<CartItemResponse> getAllCartItems() {
        return cartRepo.getAllCartItems(currentUserProvider.getCurrentUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CartItemPriceResponse updateCartItemQuantity(String cartItemId, Integer quantityUpdate) {
        UserEntity user = currentUserProvider.getCurrentUser();
        CartEntity cart = findByUserId(user.getId());

        final CartItemEntity cartItem = checkCartItemFromUser(cartItemId, cart, user);
        checkNullQuantityCartItem(cartItem, quantityUpdate);
        final ProductEntity product = cartItem.getProduct();
        productService.isProductActive(product);

        final InventoryEntity inventory = inventoryService.checkProductExistingInventory(product.getId());
        if ((quantityUpdate > 0 && inventory.getQuantity() - quantityUpdate < 0) || inventory.getInventoryStatus() == InventoryStatus.OUT_OF_STOCK) {
            throw ApplicationErrors.INVENTORY_NOT_ENOUGH;
        }

        final int newQuantity = cartItem.getQuantity() + quantityUpdate;
        BigDecimal newPrice = product.getPrice()
                .multiply(BigDecimal.valueOf(newQuantity));
        cartItem.setQuantity(newQuantity);
        cartItem.setSubtotal(newPrice);
        cartItemRepo.save(cartItem);

     calculateCartSubTotalAndSet(cart);

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
        calculateCartSubTotalAndSet(cart);

        return "Delete CartItem successful";
    }
    private void calculateCartSubTotalAndSet(CartEntity cart){
        BigDecimal cartSubTotal =  cart.getCartItemList().stream()
                .map(CartItemEntity::getSubtotal)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        cart.setSubtotal(cartSubTotal);
        cart.setTotalAmount(cartSubTotal);
    }
    @Override
    public OrderSummaryResponse createBuyNow(UserEntity user, String discountName, String productId) {
        ProductEntity product = productService.findProductById(productId);
        productService.isProductActive(product);
        CartEntity cart = findByUserId(user.getId());
        if (cart == null) {
            cart = createNewCart(user, product, AppConst.BUY_NOW_QUANTITY);
            if(discountName != null) {
                return convertToOrderSummaryResponse(user, discountName, cart);
            }
            else {
                return convertToOrderSummaryResponse(user, null, cart);
            }
        }
       else {
            addProductToCart(cart, product , AppConst.BUY_NOW_QUANTITY);

            if(discountName != null){
                return convertToOrderSummaryResponse(user, discountName , cart);

            }
            return convertToOrderSummaryResponse(user ,null, cart);
        }
    }

    private void addProductToCart(CartEntity cart, ProductEntity product, int quantity) {
        log.info("add Product to cart : {}" ,cart.getId());
        InventoryEntity inventory = inventoryService.findProductAvailability(product, quantity);
        BigDecimal subtotalNewCartItem = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        CartItemEntity cartItem= CartItemEntity.builder()
                .cart(cart)
                .product(product)
                .quantity(AppConst.BUY_NOW_QUANTITY)
                .subtotal(subtotalNewCartItem)
                .inventoryStatus(inventory.getInventoryStatus())
                .build();
        log.info("add  cartItem success with cart id : {}" , cartItem.getCart());
        cart.addCartItem(cartItem);

        BigDecimal oldCartSubtotal = cart.getSubtotal();
        BigDecimal newCartSubtotal = oldCartSubtotal.add(subtotalNewCartItem);
        cart.setSubtotal(newCartSubtotal);
        cart.setTotalAmount(newCartSubtotal);
    }

    @Override
    public OrderSummaryResponse convertToOrderSummaryResponse(UserEntity user, String discountName , CartEntity cart ) {
        BigDecimal subTotal = cart.getSubtotal();
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal deliveryAmount = deliveryService.calculateCartDeliveryAmount(user , cart);
        if(discountName == null){
            totalAmount = cart.getTotalAmount().add(deliveryAmount);
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

//        if (discount.getDiscount().getDiscountType() == DiscountType.FREE_SHIP) {
//            discountAmount =  discountService.discountFreeShipCalculation( discount,  deliveryAmount);
//            totalAmount = cart.getSubtotal().add(deliveryAmount).subtract(discountAmount);
//            return OrderSummaryResponse.builder()
//                    .subtotal(subTotal)
//                    .discountAmount(discountAmount)
//                    .deliveryAmount(deliveryAmount)
//                    .totalAmount(totalAmount)
//                    .build();
//        }
//           discountAmount = discountService.discountCalculation(discount,subTotal);
//           totalAmount = subTotal.subtract(deliveryAmount).add(deliveryAmount);
//        return OrderSummaryResponse.builder()
//                .subtotal(subTotal)
//                .discountAmount(discountAmount)
//                .deliveryAmount(deliveryAmount)
//                .totalAmount(totalAmount)
//                .build();

    }

    @Override
    public CartEntity findByUserId(String id) {
        return cartRepo.findByUser_Id(id);
    }


    @Override
    public List<CartItemEntity> loadCartItems(CartEntity cart) {
        List<CartItemEntity> cartItems = cartItemRepo.findAllForCheckout(cart.getId());
         cartItems.forEach(cartItemEntity -> checkCartItemFromUser(cartItemEntity.getId(),cart,user));
         cartItems.forEach(cartItemEntity -> productService.isProductActive(cartItemEntity.getProduct()));
        return cartItems;
    }


    private CartEntity createNewCart(UserEntity user,ProductEntity  product, int quantity) {
        InventoryEntity inventory = inventoryService.findProductAvailability(product, quantity);
//        BigDecimal deliveryAmount = deliveryService.calculateProductDeliveryAmount(user, inventory);
        BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));

      CartEntity cart =  CartEntity.builder()
                .user(user)
                .subtotal(subtotal)
                .totalAmount(subtotal)
                .cartItemList(new ArrayList<>())
                .build();
      log.info("New cart created : {} from User {}" , cart.getId() , cart.getUser().getId());
      CartItemEntity itemEntity = CartItemEntity.builder()
              .cart(cart)
              .product(product)
              .quantity(quantity)
              .subtotal(subtotal)
              .inventoryStatus(inventory.getInventoryStatus())
              .build();
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
