package com.tasks.shopping;

import com.core.MessageWrapper;
import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/9/2014.
 */
public class PutToShoppingCart implements Function<MessageWrapper, MessageWrapper> {

    @Override
    public MessageWrapper apply(MessageWrapper value) {
        String echoString = "shopping:putToTheCart";
        return MessageWrapper.wrap(value + ":" + echoString);
    }
}
