package com.designpatterns.samples.strategypattern.service;

import com.designpatterns.samples.strategypattern.dto.PaymentRequest;
import com.designpatterns.samples.strategypattern.strategy.PaymentStrategy;
import com.designpatterns.samples.strategypattern.strategy.PaymentStrategyResolver;
import org.springframework.stereotype.Service;

@Service
public class PaymentService
{
    private final PaymentStrategyResolver paymentStrategyResolver;

    public PaymentService(PaymentStrategyResolver paymentStrategyResolver) {
        this.paymentStrategyResolver = paymentStrategyResolver;
    }

    public void process(PaymentRequest paymentRequest)
    {
        PaymentStrategy paymentStrategy = paymentStrategyResolver.resolve(paymentRequest.paymentType());
        paymentStrategy.pay(paymentRequest.amount());
    }
}
