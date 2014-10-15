package com.tasks.farmwork;

import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/9/2014.
 */
public class FeedChickens implements Function<String, String> {

    @Override
    public String apply(String value) {
        String echoString = "farm:feedChickens";

        for(int i = 0; i < 5; i++){
            System.out.println("FeedChickens step executing ...");
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
