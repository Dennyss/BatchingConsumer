package com.consumer;

import reactor.function.Consumer;

import java.util.List;

/**
 * Created by Denys Kovalenko on 10/7/2014.
 */
public class BatchingConsumer implements Consumer<List<String>> {

    @Override
    public void accept(List<String> messages) {
        System.out.print("Consumed: " + messages + "\n");
    }
}
