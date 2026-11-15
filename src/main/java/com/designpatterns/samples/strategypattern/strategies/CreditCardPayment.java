package com.designpatterns.samples.strategypattern.strategies;

import com.designpatterns.samples.strategypattern.strategy.PaymentStrategy;
import org.springframework.stereotype.Component;

@Component
public class CreditCardPayment implements PaymentStrategy
{

    @Override
    public String type() {
        return "creditCard";
    }

    @Override
    public void pay(double amount) {
        System.out.printf("Paid %.2f via Credit Card%n", amount);
    }
}
