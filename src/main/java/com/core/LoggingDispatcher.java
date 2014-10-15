package com.core;

import reactor.event.Event;
import reactor.function.Consumer;

/**
 * Created by Denys Kovalenko on 10/15/2014.
 */
public class LoggingDispatcher implements Consumer<Event<String>> {

    @Override
    public void accept(Event<String> stringEvent) {
        System.out.println("LoggingDispatcher. Incoming message is: " + stringEvent.getData());
    }

}
