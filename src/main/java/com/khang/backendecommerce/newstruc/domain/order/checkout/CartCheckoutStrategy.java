package com.khang.backendecommerce.newstruc.domain.order.checkout;

import com.khang.backendecommerce.infrastructure.common.enums.CheckoutSource;
import com.khang.backendecommerce.infrastructure.configuration.CurrentUserProvider;
import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.newstruc.domain.order.dto.OrderCommand;
import com.khang.backendecommerce.newstruc.domain.order.dto.realtime.CheckoutItemSnapShot;
import com.khang.backendecommerce.newstruc.domain.order.dto.realtime.CheckoutSnapshot;
import com.khang.backendecommerce.newstruc.domain.order.dto.realtime.RecipientSnapshot;
import com.khang.backendecommerce.newstruc.domain.user.entity.UserEntity;
import com.khang.backendecommerce.newstruc.entity.CartEntity;
import com.khang.backendecommerce.newstruc.entity.CartItemEntity;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import com.khang.backendecommerce.newstruc.repo.CartItemRepository;
import com.khang.backendecommerce.newstruc.repo.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartCheckoutStrategy implements CheckoutSourceStrategy{
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final CurrentUserProvider currentUserProvider;
    @Override
    public CheckoutSource source() {
        return CheckoutSource.CART;
    }

    @Override
    public CheckoutSnapshot load(String userId, OrderCommand command) {
        CartEntity cart = cartRepo.findByIdForUpdate(userId).orElseThrow(() ->  ApplicationErrors.CART_NOT_FOUND);
        List<CartItemEntity> cartItems = cartItemRepo.findAllForCheckout(cart.getId());
        if(cartItems.isEmpty()){
            throw ApplicationErrors.CART_ITEM_NOT_FOUND;
        }
        List<CheckoutItemSnapShot> checkOutItems = cartItems.stream()
                .map(item -> {
                    ProductEntity product = item.getProduct();
                       return new CheckoutItemSnapShot(
                                item.getId() ,
                                product.getId(),
                                product.getStore().getId(),
                                product.getSku(),
                                product.getName(),
                                product.getPrice(),
                                item.getQuantity()
                        );
                }).toList();
        List<String> productsId = cartItems.stream()
                .map(item -> item.getProduct().getId()).toList();
        RecipientSnapshot recipientSnapshot = RecipientSnapshot.from(currentUserProvider.getCurrentUser());
        return new CheckoutSnapshot(userId,CheckoutSource.CART,cart.getId(), checkOutItems, productsId,recipientSnapshot, cart.getDiscount().getId() );
    }

    @Override
    public void complete(CheckoutSnapshot checkout) {
       int expectedRows = checkout.items().size();
       int deletedRows = cartItemRepo.deletePurchasedItems(checkout.cartId());
      if(expectedRows == 0){
          return;
      }
      if(expectedRows != deletedRows){
          throw ApplicationErrors.CART_CHANGED_DURING_CHECKOUT;
      }
    }

}
