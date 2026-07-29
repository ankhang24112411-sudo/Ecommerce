package com.khang.backendecommerce.api;

import com.khang.backendecommerce.domain.cart.dto.response.CartItemResponse;
import com.khang.backendecommerce.domain.cart.service.CartService;
import com.khang.backendecommerce.infrastructure.common.dto.response.ResponseData;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
@Validated
@Slf4j
@Tag(name ="CART - CONTROLLER")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @GetMapping("/mycart")
      public ResponseEntity<List<CartItemResponse>> getAllCartItems(){
          return new ResponseEntity<>(cartService.getAllCartItems() , HttpStatus.OK);
      }

}
