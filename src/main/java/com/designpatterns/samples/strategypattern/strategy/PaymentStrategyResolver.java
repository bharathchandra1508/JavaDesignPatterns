package com.designpatterns.samples.strategypattern.strategy;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PaymentStrategyResolver
{
    private final Map<String, PaymentStrategy> strategies;

    public PaymentStrategyResolver(List<PaymentStrategy> paymentStrategies) {
        this.strategies = paymentStrategies.stream()
                .collect(Collectors.toUnmodifiableMap(
                        s -> s.type().toLowerCase(),
                        Function.identity()
                ));
    }

    public PaymentStrategy resolve(String paymentType)
    {
        PaymentStrategy strategy = strategies.get(paymentType.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported payment type: " + paymentType);
        }
        return strategy;
    }
}
