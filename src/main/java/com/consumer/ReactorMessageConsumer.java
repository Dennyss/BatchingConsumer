package com.consumer;

import com.predicates.BatchingPredicate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.composable.Deferred;
import reactor.core.composable.Stream;
import reactor.event.Event;
import reactor.function.Consumer;


/**
 * Created by Denys Kovalenko on 10/7/2014.
 */
public class ReactorMessageConsumer implements Consumer<Event<String>>, InitializingBean{

    @Autowired
    private Deferred<String, Stream<String>> deferred;

    @Autowired
    private BatchingConsumer batchingConsumer;

    @Override
    public void accept(Event<String> event) {
        String message = event.getData();

        deferred.accept(message);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        deferred.compose().filter(new BatchingPredicate()).collect(5).consume(batchingConsumer);
    }
}
