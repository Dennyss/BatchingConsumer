package com.predicates;

import reactor.function.Predicate;

/**
 * Created by Denys Kovalenko on 10/7/2014.
 */
public class BatchingPredicate implements Predicate<String> {

    @Override
    public boolean test(String value) {
        return value.startsWith("batchProcessing:");
    }
}
