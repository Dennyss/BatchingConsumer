package com.tasks.shopping.grocery;

import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/15/2014.
 */
public class BuyPepsi implements Function<String, String> {

    @Override
    public String apply(String value) {
        String echoString = "grocery:pepsi";
        String derivedValue = value + ":" + echoString;

        for (int i = 0; i < 5; i++) {
            System.out.println("BuyPepsi step executing ...");
            pause(500);
        }

        return derivedValue;
    }

    private void pause(long millis) {
        try {
            Thread.currentThread().sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
