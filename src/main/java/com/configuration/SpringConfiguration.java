package com.configuration;

import com.core.FlowSpecification;
import com.core.InputDispatcher;
import com.jms.JMSMessageConsumer;
import com.jms.JMSMessageProducer;
import com.redis.RedisDAOImpl;
import com.tasks.shopping.CheckoutAllItems;
import com.tasks.shopping.PutToShoppingCart;
import com.tasks.shopping.autoparts.BuyOil;
import com.tasks.shopping.autoparts.BuyTires;
import com.tasks.farmwork.FeedChickens;
import com.tasks.farmwork.FeedPigs;
import com.tasks.farmwork.WashClothes;
import com.tasks.shopping.grocery.BuyApples;
import com.tasks.shopping.grocery.BuyBananas;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.core.composable.Deferred;
import reactor.core.composable.spec.Streams;
import reactor.core.spec.Reactors;
import redis.clients.jedis.JedisShardInfo;

/**
 * Created by Denys Kovalenko on 10/6/2014.
 */
@Configuration
@EnableAutoConfiguration
public class SpringConfiguration {
    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private String port;

    // @EnableReactor does not work
    @Bean
    public Environment createEnvironment(){
        return new Environment();
    }

    @Bean
    public Deferred createDeferred(){
        return Streams.<String>defer()
                .env(createEnvironment())
                .dispatcher(Environment.RING_BUFFER)
                .get();
    }

    @Bean
    public Reactor createReactor(Environment env) {
        return Reactors.reactor()
                .env(env)
                .dispatcher(Environment.RING_BUFFER)
                .get();
    }

    @Bean
    public FlowConfiguration createFlowConfiguration(){
        return new FlowConfiguration();
    }

    @Bean
    public JedisConnectionFactory getJedisConnectionFactory(){
        return new JedisConnectionFactory(new JedisShardInfo(host, port));
    }

    @Bean
    public RedisDAOImpl getRedisDAO(){
        return new RedisDAOImpl(getJedisConnectionFactory());
    }

    @Bean(name = "jmsMessageConsumer")
    public JMSMessageConsumer createJMSMessageConsumer(){
        return new JMSMessageConsumer();
    }

    @Bean
    @DependsOn("jmsMessageConsumer")
    public JMSMessageProducer createJMSMessageProducer(){
        return new JMSMessageProducer();
    }

    @Bean
    public InputDispatcher createInputDispatcher(){
        return new InputDispatcher();
    }

    @Bean
    public BuyApples createBuyApplesTask(){
        return new BuyApples();
    }

    @Bean
    public BuyBananas createBuyBananasTask(){
        return new BuyBananas();
    }

    @Bean
    public BuyOil createBuyOilTask(){
        return new BuyOil();
    }

    @Bean
    public BuyTires createBuyTiresTask(){
        return new BuyTires();
    }

    @Bean
    public PutToShoppingCart createPutToShoppingCart(){
        return new PutToShoppingCart();
    }

    @Bean
    public CheckoutAllItems createCheckoutAllItems(){
        return new CheckoutAllItems();
    }

    @Bean
    public FeedChickens createFeedChickens(){
        return new FeedChickens();
    }

    @Bean
    public FeedPigs createFeedPigs(){
        return new FeedPigs();
    }

    @Bean
    public WashClothes createWashClothes(){
        return new WashClothes();
    }
}