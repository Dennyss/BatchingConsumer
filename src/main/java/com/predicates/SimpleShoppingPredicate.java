package com.predicates;

import com.configuration.FlowConfiguration;
import com.core.MessageWrapper;
import reactor.function.Predicate;

/**
 * Created by Denys Kovalenko on 10/10/2014.
 */
public class SimpleShoppingPredicate implements Predicate<MessageWrapper> {

    @Override
    public boolean test(MessageWrapper value) {
        return value.getMessage().startsWith(FlowConfiguration.SIMPLE_SHOPPING_DESTINATION);
    }
}
