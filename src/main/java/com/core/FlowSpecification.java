package com.core;

import reactor.core.Environment;
import reactor.core.composable.Deferred;
import reactor.core.composable.Stream;
import reactor.core.composable.spec.Streams;
import reactor.function.Function;
import reactor.function.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denys Kovalenko on 10/7/2014.
 */
public class FlowSpecification {
    private static final int BUFFER_BACKLOG_SIZE = 10;

    private Environment environment;

    private Deferred<String, Stream<String>> firstDeferred;

    private List<Stream<String>> streams;

    public FlowSpecification(Predicate predicate, Environment environment) {
        this.environment = environment;
        streams = new ArrayList<Stream<String>>();
        firstDeferred = createNewDeferred();
        // Create new Stream, add filter and store it in list
        streams.add(firstDeferred.compose().filter(predicate));
    }

    public FlowSpecification addStep(Function step) {
        Stream<String> stream = getLastStream();
        updateLastStream(stream.map(step));
        return this;
    }

    public FlowSpecification addBatchingStep(Function step) {
        getLastStream().collect(BUFFER_BACKLOG_SIZE).map(step);
        return this;
    }

    /**
     * Use this method if you want to create separate thread pool. Please note that previous
     * thread pool DOES NOT finish. The new 'step' executes parallel together with previous.
     *
     * ------------------------------------------------------- flow one (previous steps)
     *                      \__________________________________flow two (step in parallel execution)
     *
     * @param step
     * @return
     */
    public FlowSpecification addParallelStep(Function step) {
        final Deferred<String, Stream<String>> newDeferred = createNewDeferred();
        updateLastStream(getLastStream().consume(newDeferred));
        // Continue to execute 'step' in separate thread execution
        newDeferred.compose().map(step);

        return this;
    }

    /**
     * Use this method if you want to create separate thread pool. Please note that previous
     * thread pool finishes: like a chain of thread pools.
     *
     * @return
     */
    public FlowSpecification separate() {
        Deferred<String, Stream<String>> newDeferred = createNewDeferred();
        // Connect prev execution and next
        connectExecutions(getLastStream(), newDeferred);
        streams.add(newDeferred.compose());

        return this;
    }

    public void process(String message) {
        firstDeferred.accept(message);
    }

    private void connectExecutions(Stream<String> previousStream, final Deferred<String, Stream<String>> newDeferred) {
        previousStream.consume(newDeferred);
    }

    private Deferred createNewDeferred() {
        return Streams.<String>defer()
                .env(environment)
                .dispatcher(Environment.RING_BUFFER)
                .get();
    }

    private Stream<String> getLastStream() {
        return streams.get(streams.size() - 1);
    }

    private void updateLastStream(Stream<String> stream) {
        streams.set(streams.size() - 1, stream);
    }

}
