package com.khang.backendecommerce.api;

import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import com.khang.backendecommerce.newstruc.dto.response.store.CreateProductRequest;
import com.khang.backendecommerce.newstruc.service.InventoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/seller/suborder-management")
@Validated
@Slf4j
@Tag(name ="SUB-ORDER-MANAGEMENT-CONTROLLER")
@RequiredArgsConstructor
public class InventoryManagementController {
    private final InventoryService inventoryService;
    @GetMapping("/")
    public ResponseEntity<BaseResponse<?>> createNewProduct(@RequestBody CreateProductRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(inventoryService.createProduct(request), "success"));
    }
}
