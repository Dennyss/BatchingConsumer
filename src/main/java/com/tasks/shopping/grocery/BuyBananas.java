package com.tasks.shopping.grocery;

import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/7/2014.
 */
public class BuyBananas implements Function<String, String> {

    @Override
    public String apply(String value) {
        String echoString = "grocery:bananas";
        return value + ":" + echoString;
    }
}
