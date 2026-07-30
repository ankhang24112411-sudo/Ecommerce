package com.khang.backendecommerce.domain.discount.service.impl;

import com.khang.backendecommerce.domain.discount.service.DiscountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "DISCOUNT - SERVICE")
public class DiscountServiceImpl implements DiscountService {
    @Override
    public void checkDiscountValidationFromUser(String userId, String vourcherCode) {

    }
}
