package com.khang.backendecommerce.newstruc.domain.order.rules.rule;

import org.springframework.core.Ordered;

import java.util.List;

public interface BusinessRule<C,V> extends Ordered {
    default boolean supports(C context){
        return true;
    }
    List<V> validate(C context);
}
