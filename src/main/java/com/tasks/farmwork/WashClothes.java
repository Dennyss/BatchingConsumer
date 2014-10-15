package com.tasks.farmwork;

import com.core.MessageWrapper;
import com.utils.Utils;
import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/9/2014.
 */
public class WashClothes implements Function<MessageWrapper, MessageWrapper> {

    @Override
    public MessageWrapper apply(MessageWrapper value) {
        String echoString = "farm:washingMachineStarted";

        for(int i = 0; i < 20; i++){
            System.out.println("WashClothes parallel step executing ...");
            Utils.pause(500);
        }

        return MessageWrapper.wrap(value + ":" + echoString);
    }

}