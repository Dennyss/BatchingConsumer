package com.tasks.shopping.autoparts;

import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/8/2014.
 */
public class BuyTires implements Function<String, String> {

    @Override
    public String apply(String value) {
        String echoString = "autoparts:tires";
        return value + ":" + echoString;
    }
}
