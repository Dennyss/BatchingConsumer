package com.predicates;

import com.configuration.FlowConfiguration;
import reactor.function.Predicate;

/**
 * Created by Denys Kovalenko on 10/7/2014.
 */
public class ShoppingPredicate implements Predicate<String> {

    @Override
    public boolean test(String value) {
        return value.startsWith(FlowConfiguration.SEPARATE_SHOPPING_DESTINATION);
    }
}
