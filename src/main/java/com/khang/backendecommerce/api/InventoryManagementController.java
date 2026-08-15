package com.khang.backendecommerce.api;

import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import com.khang.backendecommerce.newstruc.domain.csv.ProductExportJobService;
import com.khang.backendecommerce.newstruc.domain.csv.dto.ExportResult;
import com.khang.backendecommerce.newstruc.dto.response.store.CreateProductRequest;
import com.khang.backendecommerce.newstruc.service.InventoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/seller/inventory-management")
@Validated
@Slf4j
@Tag(name = "SUB-ORDER-MANAGEMENT-CONTROLLER")
@RequiredArgsConstructor
public class InventoryManagementController {
    private final InventoryService inventoryService;
    private final ProductExportJobService exportService;

    @GetMapping("/")
    public ResponseEntity<BaseResponse<?>> createNewProduct(@RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(inventoryService.createProduct(request), "success"));
    }

    @PostMapping("/export-csv")
    public ResponseEntity<BaseResponse<ExportResult>> exportCsv() throws Exception {

        return ResponseEntity.accepted()
                .body(new BaseResponse<>(exportService.export(), "ok"));
    }
}
