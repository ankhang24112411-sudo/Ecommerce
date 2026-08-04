package com.khang.backendecommerce.api;

import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import com.khang.backendecommerce.newstruc.dto.request.MockPaymentWebhookRequest;
import com.khang.backendecommerce.newstruc.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment/webhooks")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    public ResponseEntity<BaseResponse<Void>> mockingWebhooks(@RequestBody MockPaymentWebhookRequest request){
        paymentService.mockWebhooks(request);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(null, "Payment on the way"));
    }
}
