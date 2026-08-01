package com.khang.backendecommerce.api;

import com.khang.backendecommerce.domain.authentication.dto.request.SignInRequest;
import com.khang.backendecommerce.domain.authentication.dto.response.TokenResponse;
import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Validated
@Slf4j
@Tag(name ="Authentication Controller")
@RequiredArgsConstructor
public class OrderController {
    private final CheckoutFacade checkoutFacade;
    @PostMapping("/access")
    public ResponseEntity<BaseResponse<OrderResponse> login (@RequestBody OrderRequest request){
        return  ResponseEntity.status(HttpStatus.CREATED).(checkoutFacade.)
    }
}
