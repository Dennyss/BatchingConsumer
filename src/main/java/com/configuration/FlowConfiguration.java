package com.configuration;

import com.core.InputDispatcher;
import com.core.FlowSpecification;
import com.predicates.BatchingShoppingPredicate;
import com.predicates.ParallelWorkPredicate;
import com.predicates.SeparateShoppingPredicate;
import com.predicates.SimpleShoppingPredicate;
import com.tasks.shopping.CheckoutAllItems;
import com.tasks.shopping.PutToShoppingCart;
import com.tasks.shopping.autoparts.BuyOil;
import com.tasks.shopping.autoparts.BuyTires;
import com.tasks.farmwork.FeedChickens;
import com.tasks.farmwork.FeedPigs;
import com.tasks.farmwork.WashClothes;
import com.tasks.shopping.grocery.BuyApples;
import com.tasks.shopping.grocery.BuyBananas;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.Environment;

/**
 * Created by Denys Kovalenko on 10/8/2014.
 */
public class FlowConfiguration implements InitializingBean {
    public static final String SIMPLE_SHOPPING_DESTINATION = "simpleShopping";
    public static final String SEPARATE_SHOPPING_DESTINATION = "separateShopping";
    public static final String BATCHING_SHOPPING_DESTINATION = "batchingShopping";
    public static final String PARALLEL_WORK_DESTINATION = "parallelWork";

    @Autowired
    private InputDispatcher inputDispatcher;
    @Autowired
    private Environment env;
    @Autowired
    private BuyApples buyApples;
    @Autowired
    private BuyBananas buyBananas;
    @Autowired
    private BuyTires buyTires;
    @Autowired
    private BuyOil buyOil;
    @Autowired
    private PutToShoppingCart putToShoppingCart;
    @Autowired
    private CheckoutAllItems checkoutAllItems;
    @Autowired
    private FeedPigs feedPigs;
    @Autowired
    private FeedChickens feedChickens;
    @Autowired
    private WashClothes washClothes;


    public FlowSpecification processSimpleShopping(){
        return new FlowSpecification(new SimpleShoppingPredicate(), env)
                .addStep(buyApples)
                .addStep(buyBananas);
    }

    public FlowSpecification processSeparateShopping(){
        return new FlowSpecification(new SeparateShoppingPredicate(), env)
                .addStep(buyApples)
                .addStep(buyBananas)
                .separate()  // Let's do another shopping in separate thread pool
                .addStep(buyTires)
                .addStep(buyOil);
    }

    public FlowSpecification processBatchingShopping(){
        return new FlowSpecification(new BatchingShoppingPredicate(), env)
                .addStep(putToShoppingCart)
                // Collect 10(configured) items in the shopping cart (buffer) and then do checkout
                .addBatchingStep(checkoutAllItems);
    }

    public FlowSpecification processParallelWork(){
        return new FlowSpecification(new ParallelWorkPredicate(),env)
                .addStep(feedChickens)
                // start washing machine and continue to feed animals
                .addParallelStep(washClothes)
                .addStep(feedPigs);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        inputDispatcher.registerFlow(processSimpleShopping());
        inputDispatcher.registerFlow(processSeparateShopping());
        inputDispatcher.registerFlow(processBatchingShopping());
        inputDispatcher.registerFlow(processParallelWork());
    }
}
