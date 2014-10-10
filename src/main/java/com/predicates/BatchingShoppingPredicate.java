package com.predicates;

import com.configuration.FlowConfiguration;
import reactor.function.Predicate;

/**
 * Created by Denys Kovalenko on 10/9/2014.
 */
public class BatchingShoppingPredicate implements Predicate<String> {

    @Override
    public boolean test(String value) {
        return value.startsWith(FlowConfiguration.BATCHING_SHOPPING_DESTINATION);
    }
}
