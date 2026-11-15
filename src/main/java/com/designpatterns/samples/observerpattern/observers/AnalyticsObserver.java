package com.designpatterns.samples.observerpattern.observers;

import com.designpatterns.samples.observerpattern.event.OrderPlacedEvent;
import com.designpatterns.samples.observerpattern.observer.OrderObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsObserver implements OrderObserver
{
    private static final Logger log = LoggerFactory.getLogger(AnalyticsObserver.class);

    @Override
    public void onOrderPlaced(OrderPlacedEvent event) {
        log.info("Analytics: recorded sale of {}", String.format("%.2f", event.amount()));
    }
}
