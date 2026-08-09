package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.newstruc.entity.CartEntity;
import com.khang.backendecommerce.infrastructure.discountinfra.DiscountContext;
import com.khang.backendecommerce.infrastructure.discountinfra.DiscountStrategy;
import com.khang.backendecommerce.infrastructure.discountinfra.DiscountFactory;
import com.khang.backendecommerce.newstruc.entity.DiscountCustomerEntity;
import com.khang.backendecommerce.newstruc.entity.DiscountEntity;
import com.khang.backendecommerce.newstruc.repo.DiscountCustomerRepository;
import com.khang.backendecommerce.newstruc.repo.DiscountRepository;
import com.khang.backendecommerce.newstruc.service.DiscountService;
import com.khang.backendecommerce.infrastructure.common.enums.DiscountStatus;
import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j(topic = "DISCOUNT - SERVICE")
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final DiscountCustomerRepository discountCustomerRepo;
    private final DiscountFactory discountFactory;
    @Override
    public DiscountCustomerEntity checkDiscountValidationFromUser(String userId, String discountName) {
        DiscountEntity discount = discountRepository.findByDiscountName(discountName).orElseThrow(() ->  ApplicationErrors.DISCOUNT_NOT_FOUND);
        checkDateDiscount(discount);
        DiscountCustomerEntity discountCustomer =   discountCustomerRepo.findByCustomer_IdAndDiscount_DiscountName(userId, discountName).orElseThrow(() ->  ApplicationErrors.DISCOUNT_NOT_FOUND);
        if(discountCustomer.getDiscountQuantity() <= 0){
            throw ApplicationErrors.DISCOUNT_NOT_HAVE;
        }
        return discountCustomer;
    }

    @Override
    public void checkDateDiscount( DiscountEntity discount) {
        if (Instant.now().isAfter(discount.getValidTo()) || discount.getDiscountStatus() == DiscountStatus.INVALID || discount.getDiscountStatus() == DiscountStatus.OUT_OF_DATE) {
            throw ApplicationErrors.DISCOUNT_EXPIRED;
        }
    }
//    @Override
//    public BigDecimal discountFreeShipCalculation(DiscountCustomerEntity discount, BigDecimal deliveryAmount){
//      return  deliveryAmount.multiply(discount.getDiscountValue()).divide(BigDecimal.valueOf(100));
//    }
//    public BigDecimal discountCalculation(DiscountCustomerEntity discount,BigDecimal totalAmount ){
//        return totalAmount.multiply(discount.getDiscountValue()).divide(BigDecimal.valueOf(100));
//    }

    @Override
    public BigDecimal calculateDiscount(DiscountCustomerEntity discount, DiscountContext context ) {
        DiscountEntity discountSource = discount.getDiscount();
        DiscountStrategy strategy = discountFactory.getStrategy(discountSource.getDiscountType() );
        return strategy.calculate(discountSource , context);
    }
    @Override
    public boolean isSameDiscount(CartEntity cart, String discountName) {
        return Optional.ofNullable(cart.getDiscount())
                .map(DiscountCustomerEntity::getDiscount)
                .map(DiscountEntity::getDiscountName)
                .filter(name -> Objects.equals(name, discountName))
                .isPresent();
    }

    @Override
    public DiscountEntity findAndCheckDiscountCustomer(DiscountCustomerEntity discount) {
    return null;
    }

    @Override
    public void clearDiscountCustomer(DiscountCustomerEntity discountCustomer) {
        discountCustomer.setDiscountQuantity(-1);
    }
}
