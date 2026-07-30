package com.khang.backendecommerce.api;

import com.khang.backendecommerce.domain.ordersummary.dto.request.OrderSummaryRequest;
import com.khang.backendecommerce.domain.ordersummary.dto.response.OrderSummaryResponse;
import com.khang.backendecommerce.domain.ordersummary.service.OrderSummaryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@Validated
@Slf4j
@Tag(name ="CART- AND - BUY - CONTROLLER")
@RequiredArgsConstructor
public class CartAndBuyController {
    private final OrderSummaryService orderSummaryService;
    public ResponseEntity<OrderSummaryResponse> createOrderSummaryRequest(@RequestBody OrderSummaryRequest orderSummaryRequest){
        return new ResponseEntity<>(orderSummaryService.createOrderSummaryRequest(orderSummaryRequest) , HttpStatus.CREATED);
    }
}
