package com.khang.backendecommerce.domain.discount.service.impl;

import com.khang.backendecommerce.domain.discount.entity.DiscountCustomerEntity;
import com.khang.backendecommerce.domain.discount.entity.DiscountEntity;
import com.khang.backendecommerce.domain.discount.repo.DiscountCustomerRepository;
import com.khang.backendecommerce.domain.discount.repo.DiscountRepository;
import com.khang.backendecommerce.domain.discount.service.DiscountService;
import com.khang.backendecommerce.infrastructure.common.enums.DiscountStatus;
import com.khang.backendecommerce.infrastructure.exception.InvalidDataException;
import com.khang.backendecommerce.infrastructure.exception.RessourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@Slf4j(topic = "DISCOUNT - SERVICE")
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final DiscountCustomerRepository discountCustomerRepo;
    @Override
    public DiscountCustomerEntity checkDiscountValidationFromUser(String userId, String discountName) {
        DiscountEntity discount = discountRepository.findByDiscountName(discountName).orElseThrow(() -> new RessourceNotFoundException("Can not find Discount"));
        checkDateDiscount(discount);
        return  discountCustomerRepo.findByCustomer_IdAndDiscount_DiscountName(userId, discountName).orElseThrow(() -> new RessourceNotFoundException("User does not own this discount"));

    }

    @Override
    public void checkDateDiscount( DiscountEntity discount) {
        if (Instant.now().isAfter(discount.getValidTo()) || discount.getDiscountStatus() == DiscountStatus.INVALID || discount.getDiscountStatus() == DiscountStatus.OUT_OF_DATE) {
            throw new InvalidDataException("Discount is out of date , please choose another one");
        }
    }
    @Override
    public BigDecimal discountFreeShipCalculation(DiscountCustomerEntity discount, BigDecimal deliveryAmount){
      return  deliveryAmount.multiply(discount.getDiscountValue()).divide(BigDecimal.valueOf(100));
    }
    public BigDecimal discountCalculation(DiscountCustomerEntity discount,BigDecimal totalAmount ){
        return totalAmount.multiply(discount.getDiscountValue()).divide(BigDecimal.valueOf(100));
    }
}
