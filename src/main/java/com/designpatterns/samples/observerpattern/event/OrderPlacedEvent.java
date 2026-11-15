package com.designpatterns.samples.observerpattern.event;

public record OrderPlacedEvent(String orderId,
                               String item,
                               double amount) {
}
