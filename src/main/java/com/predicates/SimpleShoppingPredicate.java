package com.predicates;

import com.configuration.FlowConfiguration;
import reactor.function.Predicate;

/**
 * Created by Denys Kovalenko on 10/10/2014.
 */
public class SimpleShoppingPredicate implements Predicate<String> {

    @Override
    public boolean test(String value) {
        return value.startsWith(FlowConfiguration.SIMPLE_SHOPPING_DESTINATION);
    }
}
