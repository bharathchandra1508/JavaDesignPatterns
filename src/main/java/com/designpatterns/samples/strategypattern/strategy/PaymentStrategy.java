package com.designpatterns.samples.strategypattern.strategy;

public interface PaymentStrategy
{
    String type();
    void pay(double amount);
}
