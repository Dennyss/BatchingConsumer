package com.tasks.shopping.grocery;

import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/8/2014.
 */
public class BuyApples implements Function<String, String> {

    @Override
    public String apply(String value) {
        String echoString = "grocery:apples";
        return value + ":" + echoString;
    }
}
