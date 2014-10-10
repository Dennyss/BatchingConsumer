package com.tasks.farmwork;

import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/9/2014.
 */
public class WashClothes implements Function<String, String> {

    @Override
    public String apply(String value) {
        String echoString = "farm:washingMachineStarted";
        return value + ":" + echoString;
    }
}