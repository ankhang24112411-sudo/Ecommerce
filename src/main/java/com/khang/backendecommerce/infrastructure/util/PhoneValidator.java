package com.khang.backendecommerce.infrastructure.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<PhoneNumber,String> {
    @Override
    public void initialize(PhoneNumber phoneNumberNo) {
        ConstraintValidator.super.initialize(phoneNumberNo);
    }

    @Override
    public boolean isValid(String phoneNo, ConstraintValidatorContext cxt) {
        if(phoneNo == null){
            return  false ;
        }
        if(phoneNo.matches("\\d{10}")) return true;
        else return (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"));
    }
}
