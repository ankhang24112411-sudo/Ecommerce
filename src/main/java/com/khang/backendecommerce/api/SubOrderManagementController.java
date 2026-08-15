package com.khang.backendecommerce.api;

import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import com.khang.backendecommerce.newstruc.dto.response.store.SubOrderPendingResponse;
import com.khang.backendecommerce.newstruc.service.SubOrderManagementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/seller/suborder-management")
@Validated
@Slf4j
@Tag(name = "SUB-ORDER-MANAGEMENT-CONTROLLER")
@RequiredArgsConstructor
public class SubOrderManagementController {
    private final SubOrderManagementService subOrderManagementService;

    @GetMapping("/")
    public ResponseEntity<BaseResponse<?>> getAllPendingSuborders(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(subOrderManagementService.getAllPendingSuborders(pageable), "success"));
    }

    @PatchMapping("/{subOrderId}/confirm")
    public ResponseEntity<BaseResponse<?>> confirmSubOrders(@PathVariable(required = true) String subOrderId) {
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(subOrderManagementService.confirmSubOrdersStatus(subOrderId), "confirm sub-order successfully"));
    }

    @PatchMapping("/{subOrderId}/reject")
    public ResponseEntity<BaseResponse<?>> rejectSubOrders(@PathVariable(required = true) String subOrderId) {
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(subOrderManagementService.rejectSubOrders(subOrderId), "reject sub-order successfully"));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<BaseResponse<?>> getTodayDashboard() {
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(subOrderManagementService.getTodayDashboard(), "success"));
    }
}
