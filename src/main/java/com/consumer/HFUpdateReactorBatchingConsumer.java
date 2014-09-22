package com.consumer;

import reactor.event.Event;
import reactor.function.batch.BatchConsumer;
import reactor.io.Buffer;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Denys Kovalenko on 9/19/2014.
 */
public class HFUpdateReactorBatchingConsumer implements BatchConsumer<Event<String>> {
    private Buffer writeBuffer;
    private static final String DELIMITER_STRING = "DELIMITER";
    private static final Buffer DELIMITER = new Buffer(DELIMITER_STRING.length(), true);

    private static final int MESSAGE_LENGTH = 6;
    // If message length is not constant, let's assume this number as approximate
    private static final int NUMBER_OF_MESSAGES_IN_BUFFER = 4;


    public HFUpdateReactorBatchingConsumer() {

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
    public void accept(final Event<String> message) {
        String textMessage = message.getData();
        if (textMessage.length() != MESSAGE_LENGTH) {
            return;
        }

        if (writeBuffer.remaining() <= textMessage.length() + DELIMITER_STRING.length()) {
            flush();
        }

        writeBuffer.append(textMessage);
        writeBuffer.append(DELIMITER.duplicate().flip());
    }


    private void flush() {
        writeBuffer.flip();

        CountDownLatch latch = new CountDownLatch(1);

        Iterable<Buffer.View> views = writeBuffer.split(DELIMITER.duplicate().flip(), true);
        Iterator<Buffer.View> viewIterator = views.iterator();
        while (viewIterator.hasNext()) {
            String token = viewIterator.next().get().asString();
            System.out.println("The token is: " + token);
        }

        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (final Exception ex) {
            throw new RuntimeException("got ex", ex);
        }

        writeBuffer.clear();
    }

}
