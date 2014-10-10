package com.core;

import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.Environment;
import reactor.core.composable.Deferred;
import reactor.core.composable.Stream;
import reactor.core.composable.spec.Streams;
import reactor.function.Consumer;
import reactor.function.Function;
import reactor.function.Predicate;
import reactor.spring.context.config.EnableReactor;

import java.util.LinkedList;

/**
 * Created by Denys Kovalenko on 10/7/2014.
 */
public class FlowSpecification {
    private static final int BUFFER_BACKLOG_SIZE = 10;

    private Environment environment;

    private Deferred<String, Stream<String>> firstDeferred;

    private LinkedList<Stream<String>> streams;

    public FlowSpecification(Predicate predicate, Environment environment){
        this.environment = environment;
        streams = new LinkedList<Stream<String>>();
        firstDeferred = createNewDeferred();
        // Create new Stream, add filter and store it in list
        streams.add(firstDeferred.compose().filter(predicate));
    }

    public FlowSpecification addStep(Function step){
        getLastStream().map(step);
        return this;
    }

    public FlowSpecification addBatchingStep(Function step){
        getLastStream().collect(BUFFER_BACKLOG_SIZE).map(step);
        return this;
    }

    /**
     * Use this method if you want to create separate thread pool. Please note that previous
     * thread pool DOES NOT finish. The new 'step' executes parallel together with previous.
     * @param step
     * @return
     */
    public FlowSpecification addParallelStep(Function step){
        final Deferred<String, Stream<String>> newDeferred = createNewDeferred();
        getLastStream().consume(new Consumer<String>() {
            @Override
            public void accept(String message) {
                newDeferred.accept(message);
            }
        });
        // Continue to execute 'step' in separate thread execution
        newDeferred.compose().map(step);

        return this;
    }

    /**
     * Use this method if you want to create separate thread pool. Please note that previous
     * thread pool finishes: like a chain of thread pools.
     * @return
     */
    public FlowSpecification separate(){
        Deferred<String, Stream<String>> newDeferred = createNewDeferred();
        // Connect prev execution and next
        connectExecutions(getLastStream(), newDeferred);
        streams.add(newDeferred.compose());

        return this;
    }

    public void process(String message){
        firstDeferred.accept(message);
    }

    private void connectExecutions(Stream<String> previousStream, final Deferred<String, Stream<String>> newDeferred) {
        previousStream.consume(new Consumer<String>() {
            @Override
            public void accept(String s) {
                newDeferred.accept(s);
            }
        });
    }

    private Deferred createNewDeferred(){
        return Streams.<String>defer()
                .env(environment)
                .dispatcher(Environment.RING_BUFFER)
                .get();
    }

    private Stream<String> getLastStream(){
        return streams.getLast();
    }

}
