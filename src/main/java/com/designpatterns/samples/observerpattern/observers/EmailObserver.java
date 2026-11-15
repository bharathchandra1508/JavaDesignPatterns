package com.designpatterns.samples.observerpattern.observers;

import com.designpatterns.samples.observerpattern.event.OrderPlacedEvent;
import com.designpatterns.samples.observerpattern.observer.OrderObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailObserver implements OrderObserver
{
    private static final Logger log = LoggerFactory.getLogger(EmailObserver.class);

    @Override
    public void onOrderPlaced(OrderPlacedEvent event) {
        log.info("Email: confirmation sent for order {}", event.orderId());
    }
}
