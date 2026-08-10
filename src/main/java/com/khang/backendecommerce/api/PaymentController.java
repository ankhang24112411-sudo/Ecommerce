package com.khang.backendecommerce.api;

import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import com.khang.backendecommerce.newstruc.dto.request.MockPaymentWebhookRequest;
import com.khang.backendecommerce.newstruc.dto.request.MockRefundWebhookRequest;
import com.khang.backendecommerce.newstruc.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payment/webhooks")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping("/")
    public ResponseEntity<BaseResponse<String>> mockingWebhooks(@RequestBody MockPaymentWebhookRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(paymentService.mockWebhooks(request), "ok"));
    }
    public ResponseEntity<BaseResponse<?>> mockingWebhooksRefundPayment(@RequestBody MockRefundWebhookRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(paymentService.mockingWebhooksRefundPayment(request), "ok"));
    }
}
