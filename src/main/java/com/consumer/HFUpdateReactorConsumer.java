package com.consumer;

import reactor.event.Event;
import reactor.function.Consumer;
import reactor.io.Buffer;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Denys Kovalenko on 9/19/2014.
 */
public class HFUpdateReactorConsumer implements Consumer<Event<Buffer>> {


    @Override
    public void accept(Event<Buffer> bufferEvent) {
        String inputText = Arrays.toString(bufferEvent.getData().asBytes());
        System.out.println("Message received by HFUpdateReactorConsumer with text: " + inputText);
    }


}
