package com.khang.backendecommerce.api;

import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import com.khang.backendecommerce.newstruc.dto.request.MockRefundWebhookRequest;
import com.khang.backendecommerce.newstruc.dto.request.ShipperPickingRequest;
import com.khang.backendecommerce.newstruc.service.TrackingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/shipper/tracking")
@Validated
@Slf4j
@Tag(name ="SUB-ORDER-MANAGEMENT-CONTROLLER")
@RequiredArgsConstructor
public class ShipmentController {
    private final TrackingService TrackingService ;

    @PostMapping("/{trackingCode}/pickup")
    public ResponseEntity<BaseResponse<?>> pickup(@RequestParam String trackingCode, @RequestBody ShipperPickingRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(TrackingService.picking(trackingCode, request), "ok"));
    }
    @PostMapping("/{trackingCode}/shipping")
    public ResponseEntity<BaseResponse<?>> shipping(@RequestParam String trackingCode){
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(TrackingService.shipping(trackingCode), "ok"));
    }
    @PostMapping("/{trackingCode}/completed")
    public ResponseEntity<BaseResponse<?>> completed(@RequestParam String trackingCode){
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(TrackingService.completed(trackingCode), "ok"));
    }
    @PostMapping("/{trackingCode}/firstReattempt")
    public ResponseEntity<BaseResponse<?>> firstReattempt(@RequestParam String trackingCode, String message){
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(TrackingService.firstReattempt(trackingCode,message), "ok"));
    }
    @PostMapping("/{trackingCode}/reattempt")
    public ResponseEntity<BaseResponse<?>> reattempt(@RequestParam String trackingCode){
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(TrackingService.reattempt(trackingCode), "ok"));
    }
    @PostMapping("/{trackingCode}/reattempt")
    public ResponseEntity<BaseResponse<?>> failed(@RequestParam String trackingCode){
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(TrackingService.failed(trackingCode), "ok"));
    }

}
