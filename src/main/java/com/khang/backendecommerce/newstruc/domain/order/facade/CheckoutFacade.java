package com.khang.backendecommerce.newstruc.domain.order.facade;

import com.khang.backendecommerce.newstruc.domain.order.dto.request.OrderCommand;
import com.khang.backendecommerce.newstruc.domain.order.dto.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckoutFacade {
    public OrderResponse placeOrder( OrderCommand command){

        return null;
    }
}
