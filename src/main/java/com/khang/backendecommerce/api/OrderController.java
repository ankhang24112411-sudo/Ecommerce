package com.khang.backendecommerce.api;

import com.khang.backendecommerce.newstruc.domain.order.dto.request.OrderRequest;
import com.khang.backendecommerce.newstruc.domain.order.dto.response.OrderResponse;
import com.khang.backendecommerce.newstruc.domain.order.facade.CheckoutFacade;
import com.khang.backendecommerce.newstruc.domain.user.entity.UserEntity;
import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import com.khang.backendecommerce.infrastructure.configuration.CurrentUserProvider;
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
@Tag(name ="Order Controller")
@RequiredArgsConstructor
public class OrderController {
    private final CurrentUserProvider currentUserProvider;
    @PostMapping("/place-order")
    public ResponseEntity<BaseResponse<OrderResponse>> placeOrder (@RequestBody OrderRequest request){
        UserEntity user = currentUserProvider.getCurrentUser();

        return  ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>( orderService.placeOrder(request), "success"));
    }
}
