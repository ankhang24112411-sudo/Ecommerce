package com.khang.backendecommerce.domain.cart.service.impl;

import com.khang.backendecommerce.domain.cart.dto.request.CartItemPriceResponse;
import com.khang.backendecommerce.domain.cart.dto.response.CartItemResponse;
import com.khang.backendecommerce.domain.cart.entity.CartEntity;
import com.khang.backendecommerce.domain.cart.entity.CartItemEntity;
import com.khang.backendecommerce.domain.cart.projection.CartMapper;
import com.khang.backendecommerce.domain.cart.repo.CartItemRepository;
import com.khang.backendecommerce.domain.cart.repo.CartRepository;
import com.khang.backendecommerce.domain.cart.service.CartService;
import com.khang.backendecommerce.domain.delivery.service.DeliveryService;
import com.khang.backendecommerce.domain.discount.entity.DiscountCustomerEntity;
import com.khang.backendecommerce.domain.inventory.entity.InventoryEntity;
import com.khang.backendecommerce.domain.inventory.repo.InventoryRepository;
import com.khang.backendecommerce.domain.inventory.service.InventoryService;
import com.khang.backendecommerce.domain.ordersummary.dto.response.OrderSummaryResponse;
import com.khang.backendecommerce.domain.product.entity.ProductEntity;
import com.khang.backendecommerce.domain.product.repository.ProductRepository;
import com.khang.backendecommerce.domain.product.service.ProductService;
import com.khang.backendecommerce.domain.user.entity.UserEntity;
import com.khang.backendecommerce.infrastructure.common.enums.DiscountType;
import com.khang.backendecommerce.infrastructure.common.enums.InventoryStatus;
import com.khang.backendecommerce.infrastructure.configuration.CurrentUserProvider;
import com.khang.backendecommerce.infrastructure.exception.InvalidDataException;
import com.khang.backendecommerce.infrastructure.exception.RessourceNotFoundException;
import com.khang.backendecommerce.infrastructure.util.AppConst;
import com.khang.backendecommerce.infrastructure.util.ValidationUtils;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CurrentUserProvider currentUserProvider;
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final CartMapper cartMapper;
    private final InventoryService inventoryService;
    private final ProductService productService;
    private final DeliveryService deliveryService;

    @Override
    public List<CartItemResponse> getAllCartItems() {
        return cartRepo.getAllCartItems(currentUserProvider.getCurrentUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CartItemPriceResponse updateCartItemQuantity(String cartItemId, Integer quantityUpdate) {

        final CartItemEntity cartItem = checkCartItemFromUser(cartItemId, currentUserProvider);
        checkNullQuantityCartItem(cartItem, quantityUpdate);
        final ProductEntity product = cartItem.getProduct();
        productService.isProductActive(product);

        final InventoryEntity inventory = inventoryService.checkProductExistingInventory(product.getId());

        if ((quantityUpdate > 0 && inventory.getQuantity() - quantityUpdate < 0) || inventory.getInventoryStatus() == inventory.getInventoryStatus()) {
            throw new InvalidDataException("Quantity is not valid and the product will out of stock soon");
        }

        final Integer oldQuantity = cartItem.getQuantity();
        if (oldQuantity + quantityUpdate > AppConst.MAX_QUANTITY_PER_ITEM) {
            throw new InvalidDataException("Limit for update quantity for an item is 99");

        }
        cartItem.setQuantity(oldQuantity + quantityUpdate);
        final int newQuantity = cartItem.getQuantity() + quantityUpdate;
        BigDecimal newPrice = product.getPrice().multiply(BigDecimal.valueOf(newQuantity));
        cartItem.setSubtotal(newPrice);
        cartItemRepo.save(cartItem);
        return cartMapper.toCartItemPriceResponse(cartItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteCartItems(String itemId) {
        checkCartItemFromUser(itemId, currentUserProvider);
        CartItemEntity cartItem = cartItemRepo.findById(itemId).orElseThrow(() -> new RessourceNotFoundException("Cannot find your cart item"));
        cartItemRepo.delete(cartItem);
        return "Delete CartItem successful";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderSummaryResponse createBuyNow(UserEntity user, DiscountCustomerEntity discount, String productId) {
        ProductEntity product = productService.findProductById(productId);
        productService.isProductActive(product);
        CartEntity cart = cartRepo.findByUserId(user.getId());

        if (cart == null) {
            cart = createNewCart(user, product, AppConst.BUY_NOW_QUANTITY);
            if(discount != null){
                cart.setDiscount(discount);
            }
            return convertToOrderSummaryResponse(cart, discount);
        }
       else {
            addProductToCart(user ,cart, product , AppConst.BUY_NOW_QUANTITY);
//            if(cart.getDiscount() != null && discount != null){
//                boolean sameDis = cart.getDiscount().getId().equals(discount.getDiscount().getId());
//                if(!sameDis){
//                    cart.setDiscount(discount);
//                    return convertToOrderSummaryResponse(cart,cart.getDiscount());
//
//                }
//            }else if (cart.getDiscount() == null && discount != null){
//                return convertToOrderSummaryResponse(cart,discount);
//
//            }
        }

    }

    private void addProductToCart(UserEntity user ,CartEntity cart, ProductEntity product, int quantity) {
        InventoryEntity inventory = inventoryService.findProductAvailability(product, quantity);
        BigDecimal deliveryAmount = deliveryService.calculateProductDeliveryAmount(user, inventory);
        BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        CartItemEntity cartItem = CartItemEntity.builder()
                .cart(cart).product(product).quantity(1).subtotal(subtotal).inventoryStatus(inventory.getInventoryStatus())
                .build();
        cart.addCartItem(cartItem);
    }

    private OrderSummaryResponse convertToOrderSummaryResponse(CartEntity entity, DiscountCustomerEntity discount) {
        BigDecimal totalDiscountAmount = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal deliveryAmount = deliveryService.calculateProductDeliveryAmount(user, inventory);
       List<InventoryEntity> inventoryEntities = entity.getCartItemList().stream()
               .map(cartItem -> cartItem.getProduct())
               .map(product -> product.getId())
        if(discount == null){
            return   OrderSummaryResponse.
        }
        if (discount.getDiscount().getDiscountType() == DiscountType.FREE_SHIP) {
            totalDiscountAmount = deliveryAmount.multiply(discount.getDiscountValue())
                    .divide(BigDecimal.valueOf(100));
            deliveryAmount = deliveryAmount.subtract(totalDiscountAmount);
            totalAmount = entity.getSubtotal().add(deliveryAmount);
            return OrderSummaryResponse.builder()
                    .subtotal(entity.getSubtotal())
                    .discountAmount(totalDiscountAmount)
                    .deliveryAmount(deliveryAmount)
                    .totalAmount(totalAmount)
                    .build();

        }
    }


    private CartEntity createNewCart(UserEntity user,ProductEntity  product, int quantity) {
        InventoryEntity inventory = inventoryService.findProductAvailability(product, quantity);
//        BigDecimal deliveryAmount = deliveryService.calculateProductDeliveryAmount(user, inventory);
        BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
//        BigDecimal totalAmount = subtotal.add(deliveryAmount);

      CartEntity cart =  CartEntity.builder()
                .user(user)
                .subtotal(subtotal)
                .totalAmount(subtotal)
                .build();
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

    private CartItemEntity checkCartItemFromUser(String cartItemId,CurrentUserProvider currentUserProvider){
        String userId = currentUserProvider.getCurrentUserId();
        return cartItemRepo.findByIdAndCart_User_Id(cartItemId , userId).orElseThrow(() -> new RessourceNotFoundException("Cart item not exists"));
    }
}
