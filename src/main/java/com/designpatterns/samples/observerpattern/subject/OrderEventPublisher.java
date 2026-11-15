package com.designpatterns.samples.observerpattern.subject;

import com.designpatterns.samples.observerpattern.event.OrderPlacedEvent;
import com.designpatterns.samples.observerpattern.observer.OrderObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderEventPublisher
{
    private static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);

    private final List<OrderObserver> observers;

    public OrderEventPublisher(List<OrderObserver> observers) {
        this.observers = List.copyOf(observers);
        log.info("Registered {} order observers", this.observers.size());
    }

    public void publish(OrderPlacedEvent event)
    {
        for(OrderObserver observer : observers)
        {
            try {
                observer.onOrderPlaced(event);
            } catch (RuntimeException e) {
                log.error("Observer {} failed for order {}: {}",
                        observer.getClass().getSimpleName(), event.orderId(), e.getMessage());
            }
        }
    }
}
