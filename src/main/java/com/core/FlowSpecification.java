package com.core;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import reactor.core.Environment;
import reactor.core.composable.Deferred;
import reactor.core.composable.Stream;
import reactor.core.composable.spec.Streams;
import reactor.event.dispatch.Dispatcher;
import reactor.event.dispatch.RingBufferDispatcher;
import reactor.event.dispatch.WorkQueueDispatcher;
import reactor.function.Function;
import reactor.function.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denys Kovalenko on 10/7/2014.
 */
public class FlowSpecification {
    private static int MAX_PARALLEL_FLOWS = 16;
    // Buffer related parameters (for batching processing)
    private static final int BUFFER_BACKLOG_SIZE = 10;
    private static final int BUFFER_FLUSH_TIMEOUT = 2000;  // 2 sec

    private Environment environment;
    private Deferred<String, Stream<String>> firstDeferred;
    private List<Stream<String>> streams;

    public FlowSpecification(Predicate predicate, Environment environment) {
        this.environment = environment;
        streams = new ArrayList<Stream<String>>();
        firstDeferred = createNewDeferred(createRingBufferDispatcher());
        // Create new Stream, add filter and store it in list
        streams.add(firstDeferred.compose().filter(predicate));
    }

    public FlowSpecification addStep(Function step) {
        Stream<String> stream = getLastStream();
        updateLastStream(stream.map(step));
        return this;
    }

    public FlowSpecification addBatchingStep(Function step) {
        updateLastStream(getLastStream()
                // Flush buffer by quantity of messages
                .collect(BUFFER_BACKLOG_SIZE)
                // Flush buffer by timeout
                .timeout(BUFFER_FLUSH_TIMEOUT).flush()
                .map(step));
        return this;
    }

    /**
     * Use this method if you want to create separate thread pool. Please note that previous
     * thread pool DOES NOT finish. The new 'step' executes parallel together with previous one.
     * <p/>
     * _________________________________________________ flow one (previous steps)
     *                      \____________________________flow two (step in parallel execution)
     *
     * @param step
     * @return
     */
    public FlowSpecification addParallelStep(Function step) {
        Deferred<String, Stream<String>> newDeferred = createNewDeferred(createWorkQueueDispatcher());
        updateLastStream(getLastStream().consume(newDeferred));
        // Continue to execute 'step' in separate thread execution
        newDeferred.compose().map(step);

        return this;
    }

    /**
     * Use this method if you want to create separate thread pool. Please note that previous
     * thread pool finishes: it's like a chain of thread pools.
     *
     * @return
     */
    public FlowSpecification separate() {
        Deferred<String, Stream<String>> newDeferred = createNewDeferred(createRingBufferDispatcher());
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

    private Deferred createNewDeferred(Dispatcher dispatcher) {
        return Streams.<String>defer()
                .env(environment)
                .dispatcher(dispatcher)
                .get();
    }

    private Stream<String> getLastStream() {
        return streams.get(streams.size() - 1);
    }

    private void updateLastStream(Stream<String> stream) {
        streams.set(streams.size() - 1, stream);
    }

    private Dispatcher createRingBufferDispatcher() {
        return new RingBufferDispatcher("RingBufferDispatcher",
                MAX_PARALLEL_FLOWS * 4,
                null,
                ProducerType.MULTI,
                new BlockingWaitStrategy());
    }

    private Dispatcher createWorkQueueDispatcher() {
        return new WorkQueueDispatcher("WorkQueueDispatcher",
                MAX_PARALLEL_FLOWS,
                MAX_PARALLEL_FLOWS * 8,
                null);
    }
}
