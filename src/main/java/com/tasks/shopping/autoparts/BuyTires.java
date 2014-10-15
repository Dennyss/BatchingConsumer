package com.tasks.shopping.autoparts;

import com.core.MessageWrapper;
import com.utils.Utils;
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
            Utils.pause(500);
        }

        return MessageWrapper.wrap(value + ":" + echoString);
    }

}
