package com.consumer;

import reactor.function.Consumer;

/**
 * Created by Denys Kovalenko on 10/7/2014.
 */
public class FinalConsumer implements Consumer<String> {

    @Override
    public void accept(String s) {
        System.out.println(s + ": finalConsumer.");
    }
}
