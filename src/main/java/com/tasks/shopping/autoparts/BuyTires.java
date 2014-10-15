package com.tasks.shopping.autoparts;

import com.core.MessageWrapper;
import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/8/2014.
 */
public class BuyTires implements Function<MessageWrapper, MessageWrapper> {

    @Override
    public MessageWrapper apply(MessageWrapper value) {
        String echoString = "autoparts:tires";

        for (int i = 0; i < 5; i++) {
            System.out.println("BuyTires step executing ...");
            pause(500);
        }

        return MessageWrapper.wrap(value + ":" + echoString);
    }

    private void pause(long millis) {
        try {
            Thread.currentThread().sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
