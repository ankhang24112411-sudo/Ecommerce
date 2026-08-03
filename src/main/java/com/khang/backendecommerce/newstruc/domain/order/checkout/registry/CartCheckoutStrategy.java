package com.khang.backendecommerce.newstruc.domain.order.checkout.registry;

import com.khang.backendecommerce.infrastructure.common.enums.CheckoutSource;
import com.khang.backendecommerce.infrastructure.configuration.CurrentUserProvider;
import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.newstruc.domain.order.checkout.CheckoutSourceStrategy;
import com.khang.backendecommerce.newstruc.domain.order.dto.request.OrderCommand;
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
public class CartCheckoutStrategy implements CheckoutSourceStrategy {
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final CurrentUserProvider currentUserProvider;
    @Override
    public CheckoutSource source() {
        return CheckoutSource.CART;
    }

    @Override
    public CheckoutContext load(String userId, OrderCommand command) {
        return null;
    }


//    @Override
//    public CheckoutContext load(String userId, OrderCommand command) {
//        UserEntity user = currentUserProvider.getCurrentUser();
//        CartEntity cart = cartRepo.findByIdForUpdate(userId).orElseThrow(() ->  ApplicationErrors.CART_NOT_FOUND);
//        List<CartItemEntity> cartItems = cartItemRepo.findAllForCheckout(cart.getId());
//
//        List<CheckoutItemSnapshot> checkOutItems = cartItems.stream()
//                .map(item -> {
//                    ProductEntity product = item.getProduct();
//                       return new CheckoutItemSnapShot(
//                                item.getId() ,
//                                product.getId(),
//                                product.getStore().getId(),
//                               item.getQuantity(),
//                               product.getPrice(),
//                                product.getName(),
//                        );
//                }).toList();
//        List<String> productsId = cartItems.stream()
//                .map(item -> item.getProduct().getId()).toList();
//        return new CheckoutContext(userId,CheckoutSource.CART,cart.getId(), checkOutItems, productsId, cart.getDiscount().getId() );
//    }

    @Override
    public void complete(CheckoutContext context) {
       int expectedRows = context.items().size();
       int deletedRows = 1;
      if(expectedRows == 0){
          return;
      }
      if(expectedRows != deletedRows){
          throw ApplicationErrors.CART_CHANGED_DURING_CHECKOUT;
      }
    }

}
