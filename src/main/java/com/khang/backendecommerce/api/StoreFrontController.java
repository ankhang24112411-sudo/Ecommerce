package com.khang.backendecommerce.api;

import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import com.khang.backendecommerce.newstruc.dto.request.OrderSummaryRequest;
import com.khang.backendecommerce.newstruc.dto.response.OrderSummaryResponse;
import com.khang.backendecommerce.newstruc.dto.response.StoreFrontHomeResponse;
import com.khang.backendecommerce.newstruc.service.StoreFrontService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/storefront/home")
@Validated
@Slf4j
@Tag(name ="STORE-FRONT-CONTROLLER")
@RequiredArgsConstructor
public class StoreFrontController {
    private final StoreFrontService storeFrontService;
    @GetMapping("/banner-features")
    ResponseEntity<BaseResponse<StoreFrontHomeResponse>> getBannerAndFeature(@RequestBody OrderSummaryRequest orderSummaryRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(storeFrontService.getStoreFront(orderSummaryRequest), "success"));
    }
    @GetMapping("/search")

    public  ResponseEntity<BaseResponse<?>> advanceSearchWithSpecificationsProduct(Pageable pageable,
                                                           @RequestParam(required = false) String[] product, String [] store) {
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(storeFrontService.advanceSearchWithSpecificationsProduct(pageable,product,store), "success"));
    }
}