package com.consumer;

import com.functions.FirstFunction;
import com.functions.SecondFunction;
import com.functions.SeparateFunction;
import com.predicates.BatchingPredicate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import reactor.core.Environment;
import reactor.core.composable.Deferred;
import reactor.core.composable.Stream;
import reactor.core.composable.spec.Streams;
import reactor.event.Event;
import reactor.function.Consumer;


/**
 * Created by Denys Kovalenko on 10/7/2014.
 */
public class ReactorMessageConsumer implements Consumer<Event<String>>, InitializingBean{

    @Autowired
    @Qualifier("firstDef")
    private Deferred<String, Stream<String>> deferred;

    @Autowired
    @Qualifier("secondDef")
    private Deferred<String, Stream<String>> deferred2;

    @Autowired
    private BatchingConsumer batchingConsumer;

    @Override
    public void accept(Event<String> event) {
        String message = event.getData();

        deferred.accept(message);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Stream<String> stream = deferred.compose();
        stream.map(new FirstFunction())
                .map(new SecondFunction())
                .filter(new BatchingPredicate())
                .collect(1).consume(batchingConsumer);


        Stream stream2 = deferred2.compose()
                .map(new SeparateFunction()).consume(new FinalConsumer());

        stream.consume(new Consumer<String>() {
            @Override
            public void accept(String message) {
                deferred2.accept(message);
            }
        });


    }
}
