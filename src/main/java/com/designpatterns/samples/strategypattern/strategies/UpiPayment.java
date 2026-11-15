package com.designpatterns.samples.strategypattern.strategies;

import com.designpatterns.samples.strategypattern.strategy.PaymentStrategy;
import org.springframework.stereotype.Component;

@Component
public class UpiPayment implements PaymentStrategy
{
    @Override
    public String type() {
        return "upi";
    }

    @Override
    public void pay(double amount) {
        System.out.printf("Paid %.2f via UPI%n", amount);
    }
}
