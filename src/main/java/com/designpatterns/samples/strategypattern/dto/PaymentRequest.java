package com.designpatterns.samples.strategypattern.dto;

public record PaymentRequest(String paymentType,
                             double amount) {
}
