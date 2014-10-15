package com.core;

import reactor.event.Event;
import reactor.function.Consumer;

/**
 * Created by Denys Kovalenko on 10/15/2014.
 */
public class LoggingDispatcher implements Consumer<Event<MessageWrapper>> {

    @Override
    public void accept(Event<MessageWrapper> stringEvent) {
        System.out.println("LoggingDispatcher. Incoming message is: " + stringEvent.getData());
    }

}
