package com.khang.backendecommerce.domain.discount.service.impl;

import com.khang.backendecommerce.domain.discount.entity.DiscountEntity;
import com.khang.backendecommerce.domain.discount.repo.DiscountRepository;
import com.khang.backendecommerce.domain.discount.service.DiscountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "DISCOUNT - SERVICE")
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    @Override
    public void checkDiscountValidationFromUser(String userId, String discountName) {

    }

    @Override
    public void checkDateDiscount(String discountName) {
        DiscountEntity discount = discountRepository.findByDiscountName(discountName);
        if(discount.)
    }
}
