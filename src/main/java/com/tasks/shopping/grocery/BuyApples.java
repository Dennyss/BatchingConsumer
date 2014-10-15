package com.tasks.shopping.grocery;

import com.core.MessageWrapper;
import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/8/2014.
 */
public class BuyApples implements Function<MessageWrapper, MessageWrapper> {

    @Override
    public MessageWrapper apply(MessageWrapper value) {
        String echoString = "grocery:apples";
        return MessageWrapper.wrap(value + ":" + echoString);
    }
}
