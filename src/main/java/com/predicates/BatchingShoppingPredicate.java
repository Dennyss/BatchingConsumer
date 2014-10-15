package com.predicates;

import com.configuration.FlowConfiguration;
import com.core.MessageWrapper;
import reactor.function.Predicate;

/**
 * Created by Denys Kovalenko on 10/9/2014.
 */
public class BatchingShoppingPredicate implements Predicate<MessageWrapper> {

    @Override
    public boolean test(MessageWrapper value) {
        return value.getMessage().startsWith(FlowConfiguration.BATCHING_SHOPPING_DESTINATION);
    }
}
