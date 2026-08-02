package com.khang.backendecommerce.newstruc.domain.order.rules.config;

import com.khang.backendecommerce.newstruc.domain.order.dto.realtime.CheckoutContext;
import com.khang.backendecommerce.newstruc.domain.order.rules.CheckoutRule;
import com.khang.backendecommerce.newstruc.domain.order.rules.RuleChain;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class CheckoutRuleConfiguration {
    @Bean
    public RuleChain<CheckoutContext, CheckoutViolation, CheckoutRule> checkoutRuleRuleChain(List<CheckoutRule> rules){
        return new RuleChain<>(rules);
    }
}
