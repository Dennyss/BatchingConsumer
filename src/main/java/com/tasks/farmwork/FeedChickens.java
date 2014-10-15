package com.tasks.farmwork;

import com.core.MessageWrapper;
import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/9/2014.
 */
public class FeedChickens implements Function<MessageWrapper, MessageWrapper> {

    @Override
    public MessageWrapper apply(MessageWrapper value) {
        String echoString = "farm:feedChickens";

        for(int i = 0; i < 5; i++){
            System.out.println("FeedChickens step executing ...");
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
