package com.khang.backendecommerce.newstruc.domain.order.rules;

import com.khang.backendecommerce.newstruc.domain.order.rules.rule.BusinessRule;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
//C = Context chain nhận
//        V = Violation chain trả
//R = loại rule trong chain
public final class RuleChain <C, V , R extends BusinessRule<C,V>>{
    private final List<R> rules;
    public RuleChain(List<R> rules){
        Objects.requireNonNull(rules);
        this.rules = rules.stream()
                .sorted(Comparator.comparingInt(Ordered::getOrder))
                .toList();
    }

    public List<V> validate(C context) {
        Objects.requireNonNull(context, "Context must not be null");

        List<V> violations = new ArrayList<>();
        for (R rule : rules) {
            if (!rule.supports(context)) {
                continue;
            }

            List<V> ruleViolations = rule.validate(context);
            if (ruleViolations == null || ruleViolations.isEmpty()) {
                continue;
            }
            violations.addAll(ruleViolations);
        }
        return List.copyOf(violations);
    }

}
