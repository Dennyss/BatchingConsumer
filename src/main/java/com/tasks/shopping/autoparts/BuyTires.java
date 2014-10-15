package com.tasks.shopping.autoparts;

import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/8/2014.
 */
public class BuyTires implements Function<String, String> {

    @Override
    public String apply(String value) {
        String echoString = "autoparts:tires";

        for (int i = 0; i < 5; i++) {
            System.out.println("BuyTires step executing ...");
            pause(500);
        }

        return value + ":" + echoString;
    }

    private void pause(long millis) {
        try {
            Thread.currentThread().sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
