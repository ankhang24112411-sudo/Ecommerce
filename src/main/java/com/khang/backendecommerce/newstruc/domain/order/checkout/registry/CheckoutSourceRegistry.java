package com.khang.backendecommerce.newstruc.domain.order.checkout.registry;

import com.khang.backendecommerce.infrastructure.common.enums.CheckoutSource;
import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import com.khang.backendecommerce.newstruc.domain.order.checkout.CheckoutSourceStrategy;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class CheckoutSourceRegistry {

    private final Map<CheckoutSource, CheckoutSourceStrategy> strategies;

    public CheckoutSourceRegistry(List<CheckoutSourceStrategy> strategies) {
        EnumMap<CheckoutSource, CheckoutSourceStrategy> strategyMap = new EnumMap<>(CheckoutSource.class);
        for (CheckoutSourceStrategy strategy : strategies) {
            CheckoutSourceStrategy previous = strategyMap.put(strategy.source(), strategy);

            if (previous != null) {
                throw new IllegalStateException("Duplicate CheckoutSourceStrategy: " + strategy.source()
                );
            }
        }
        this.strategies = Map.copyOf(strategyMap);
    }

    public CheckoutSourceStrategy getRequired(CheckoutSource source) {
        CheckoutSourceStrategy strategy =
                strategies.get(source);

        if (strategy == null) {
            throw ApplicationErrors.CART_CHANGED_DURING_CHECKOUT;
        }

        return strategy;
    }
}