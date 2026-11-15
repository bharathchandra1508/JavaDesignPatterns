package com.designpatterns.samples.observerpattern.service;

import com.designpatterns.samples.observerpattern.event.OrderPlacedEvent;
import com.designpatterns.samples.observerpattern.event.OrderRequest;
import com.designpatterns.samples.observerpattern.subject.OrderEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService
{
    private final OrderEventPublisher publisher;

    public OrderService(OrderEventPublisher publisher) {
        this.publisher = publisher;
    }

    public String placeOrder(OrderRequest request) {
        String orderId = UUID.randomUUID().toString();
        // persist the order to a database here (omitted in this sample)
        publisher.publish(new OrderPlacedEvent(orderId, request.item(), request.amount()));
        return orderId;
    }
}
