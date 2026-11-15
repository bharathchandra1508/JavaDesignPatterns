package com.designpatterns.samples.strategypattern.strategies;

import com.designpatterns.samples.strategypattern.strategy.PaymentStrategy;
import org.springframework.stereotype.Component;

@Component
public class ApplePayPayment implements PaymentStrategy
{
    @Override
    public String type() {
        return "applePay";
    }

    @Override
    public void pay(double amount) {
        System.out.printf("Paid %.2f via Apple Pay%n", amount);
    }
}
