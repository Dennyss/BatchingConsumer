package com.predicates;

import com.configuration.FlowConfiguration;
import reactor.function.Predicate;

/**
 * Created by Denys Kovalenko on 10/9/2014.
 */
public class ParallelWorkPredicate implements Predicate<String> {

    @Override
    public boolean test(String value) {
        return value.startsWith(FlowConfiguration.PARALLEL_WORK_DESTINATION);
    }
}
