package com.consumer;

import reactor.function.batch.BatchConsumer;
import reactor.io.Buffer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Denys Kovalenko on 9/23/2014.
 */
public abstract class AbstractBatchingConsumer implements BatchConsumer<String> {
    private Buffer writeBuffer;
    private static final String DELIMITER_STRING = "DELIMITER";
    private static final Buffer DELIMITER = new Buffer(DELIMITER_STRING.length(), true);
    private static final int BUFFERING_TIME_INTERVAL = 1; // in seconds
    private static final int MESSAGE_LENGTH = 12;
    // If message length is not constant, let's assume this number as approximate
    private static final int NUMBER_OF_MESSAGES_IN_BUFFER = 4;


    public AbstractBatchingConsumer(){
        writeBuffer = new Buffer((MESSAGE_LENGTH + DELIMITER_STRING.length()) * NUMBER_OF_MESSAGES_IN_BUFFER, true);
        DELIMITER.append(DELIMITER_STRING);
    }

    @Override
    public void start() {
    }

    @Override
    public void end() {
        flush();
    }

    @Override
    public void accept(String textMessage) {
//        if (textMessage.length() != MESSAGE_LENGTH) {
//            return;
//        }

        if (writeBuffer.remaining() <= textMessage.length() + DELIMITER_STRING.length()) {
            flush();
        }

        writeBuffer.append(textMessage);
        writeBuffer.append(DELIMITER.duplicate().flip());
    }


    private void flush() {
        writeBuffer.flip();

        Iterable<Buffer.View> views = writeBuffer.split(DELIMITER.duplicate().flip(), true);
        List<String> messages = new ArrayList<>();
        for(Buffer.View view : views){
            String message = view.get().asString();
            messages.add(message);
        }

        process(messages);

        CountDownLatch latch = new CountDownLatch(1);
        latch.countDown();
        try {
            latch.await(BUFFERING_TIME_INTERVAL, TimeUnit.SECONDS);
        } catch (final Exception ex) {
            throw new RuntimeException("got ex", ex);
        }

        writeBuffer.clear();
    }

    public abstract void process(List<String> messages);
}