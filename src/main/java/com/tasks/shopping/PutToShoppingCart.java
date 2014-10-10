package com.tasks.shopping;

import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/9/2014.
 */
public class PutToShoppingCart implements Function<String, String> {

    @Override
    public String apply(String value) {
        String echoString = "shopping:putToTheCart";
        return value + ":" + echoString;
    }
}
