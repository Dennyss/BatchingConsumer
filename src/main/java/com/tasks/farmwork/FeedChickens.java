package com.tasks.farmwork;

import com.core.MessageWrapper;
import com.utils.Utils;
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
            Utils.pause(500);
        }

        return MessageWrapper.wrap(value + ":" + echoString);
    }

}
