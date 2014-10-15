package com.tasks.shopping.grocery;

import com.core.MessageWrapper;
import com.utils.Utils;
import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/15/2014.
 */
public class BuyPepsi implements Function<MessageWrapper, MessageWrapper> {

    @Override
    public MessageWrapper apply(MessageWrapper value) {
        String echoString = "grocery:pepsi";
        String derivedValue = value + ":" + echoString;

        for (int i = 0; i < 5; i++) {
            System.out.println("BuyPepsi step executing ...");
            Utils.pause(500);
        }

        return MessageWrapper.wrap(derivedValue);
    }

}
