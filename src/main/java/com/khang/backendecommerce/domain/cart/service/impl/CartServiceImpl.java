package com.khang.backendecommerce.domain.cart.service.impl;

import com.khang.backendecommerce.domain.cart.dto.request.CartItemPriceResponse;
import com.khang.backendecommerce.domain.cart.dto.response.CartItemResponse;
import com.khang.backendecommerce.domain.cart.entity.CartEntity;
import com.khang.backendecommerce.domain.cart.entity.CartItemEntity;
import com.khang.backendecommerce.domain.cart.projection.CartMapper;
import com.khang.backendecommerce.domain.cart.repo.CartItemRepository;
import com.khang.backendecommerce.domain.cart.repo.CartRepository;
import com.khang.backendecommerce.domain.cart.service.CartService;
import com.khang.backendecommerce.domain.discount.entity.DiscountCustomerEntity;
import com.khang.backendecommerce.domain.inventory.entity.InventoryEntity;
import com.khang.backendecommerce.domain.inventory.repo.InventoryRepository;
import com.khang.backendecommerce.domain.inventory.service.InventoryService;
import com.khang.backendecommerce.domain.ordersummary.dto.response.OrderSummaryResponse;
import com.khang.backendecommerce.domain.product.entity.ProductEntity;
import com.khang.backendecommerce.domain.user.entity.UserEntity;
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
    private final  CurrentUserProvider currentUserProvider;
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final InventoryRepository inventoryRepo;
    private final CartMapper cartMapper;
    private final InventoryService inventoryService;
    @Override
    public List<CartItemResponse> getAllCartItems() {
        return cartRepo.getAllCartItems(currentUserProvider.getCurrentUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CartItemPriceResponse updateCartItemQuantity(String cartItemId, Integer quantityUpdate) {

        final  CartItemEntity cartItem =   checkCartItemFromUser(cartItemId , currentUserProvider);
        checkNullQuantity(cartItem, quantityUpdate);
     final   ProductEntity product = cartItem.getProduct();
     final   InventoryEntity inventory = inventoryService.checkProductExistingInventory(product.getId());

        if (quantityUpdate > 0 && inventory.getQuantity() - quantityUpdate <= 0) {
            throw new InvalidDataException("Quantity is not valid and the product will out of stock soon");
        }

        final  Integer oldQuantity =  cartItem.getQuantity();
        if(oldQuantity + quantityUpdate > AppConst.MAX_QUANTITY_PER_ITEM){
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
        checkCartItemFromUser(itemId , currentUserProvider);
        CartItemEntity cartItem = cartItemRepo.findById(itemId).orElseThrow(() -> new RessourceNotFoundException("Cannot find your cart item"));
        cartItemRepo.delete(cartItem);
        return "Delete CartItem successful";
    }

    @Override
    public OrderSummaryResponse createBuyNow(UserEntity user, DiscountCustomerEntity discount, String productId) {
        CartEntity cart = ca
        return null;
    }


    public void checkNullQuantity(CartItemEntity cartItem, int quantityUpdate){
        if(quantityUpdate < 0 && cartItem.getQuantity() - quantityUpdate <= 0){
            cartItemRepo.delete(cartItem);
        }
    }

    public CartItemEntity checkCartItemFromUser(String cartItemId,CurrentUserProvider currentUserProvider){
        String userId = currentUserProvider.getCurrentUserId();
        return cartItemRepo.findByIdAndCart_User_Id(cartItemId , userId).orElseThrow(() -> new RessourceNotFoundException("Cart item not exists"));
    }
}
