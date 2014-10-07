package com.functions;

import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/7/2014.
 */
public class SecondFunction implements Function<String, String> {

    @Override
    public String apply(String value) {
        return value + " helloFromSecondFunction:";
    }
}