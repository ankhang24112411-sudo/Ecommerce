package com.khang.backendecommerce.api;

import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import com.khang.backendecommerce.newstruc.dto.request.OrderSummaryRequest;
import com.khang.backendecommerce.newstruc.dto.response.OrderSummaryResponse;
import com.khang.backendecommerce.newstruc.dto.response.StoreFrontHomeResponse;
import com.khang.backendecommerce.newstruc.service.StoreFrontService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/storefront/home")
@Validated
@Slf4j
@Tag(name ="STORE-FRONT-CONTROLLER")
@RequiredArgsConstructor
public class StoreFrontController {
    private final StoreFrontService storeFrontService;

    ResponseEntity<BaseResponse<StoreFrontHomeResponse>> createOrderSummaryRequest(@RequestBody OrderSummaryRequest orderSummaryRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(storeFrontService.getStoreFront(orderSummaryRequest), "success"));
    }
}