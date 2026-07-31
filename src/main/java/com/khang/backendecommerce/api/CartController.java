package com.khang.backendecommerce.api;

import com.khang.backendecommerce.domain.cart.dto.request.CartItemPriceResponse;
import com.khang.backendecommerce.domain.cart.dto.request.CartItemQuantityUpdate;
import com.khang.backendecommerce.domain.cart.dto.response.CartItemResponse;
import com.khang.backendecommerce.domain.cart.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@Validated
@Slf4j
@Tag(name ="CART - CONTROLLER")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @GetMapping("/")
      public ResponseEntity<List<CartItemResponse>> getAllCartItems(){
          return new ResponseEntity<>(cartService.getAllCartItems() , HttpStatus.OK);
      }
    @PatchMapping("/{itemId}/quantity")
    public ResponseEntity<CartItemPriceResponse> updateCartItemQuantity(@PathVariable @NonNull String itemId,
                                                                        @RequestBody CartItemQuantityUpdate request){
        return new ResponseEntity<>(cartService.updateCartItemQuantity(itemId, request.getQuantity()) , HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/{itemId}/delete")
    public ResponseEntity<String> deleteCartItem(@PathVariable @NonNull String itemId){
        return new ResponseEntity<>(cartService.deleteCartItems(itemId) , HttpStatus.OK);
    }
}
