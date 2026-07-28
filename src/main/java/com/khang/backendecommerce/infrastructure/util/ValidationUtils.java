package com.khang.backendecommerce.infrastructure.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

import java.util.function.Predicate;
import java.util.function.Supplier;

@UtilityClass
public  class ValidationUtils {
   public void throwIf(boolean condition , Supplier<? extends RuntimeException> exception){
       if(condition){
           throw exception.get();
       }
   }
   public  <T> void throwIf(T value , Predicate<T> condition , Supplier<? extends RuntimeException> exception){
       if(condition.test(value)){
           throw exception.get();
       }
   }
}
