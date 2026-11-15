package com.designpatterns.samples.observerpattern.observer;

import com.designpatterns.samples.observerpattern.event.OrderPlacedEvent;

public interface OrderObserver
{
    void onOrderPlaced(OrderPlacedEvent event);
}
