package com.configuration;

import com.consumer.BatchingConsumer;
import com.consumer.ReactorMessageConsumer;
import com.jms.JMSMessageConsumer;
import com.jms.JMSMessageProducer;
import com.redis.RedisDAOImpl;
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
    public Deferred createStream(){
        return Streams.<String>defer()
                .env(createEnvironment())
                .dispatcher(Environment.RING_BUFFER)
                .get();
    }

    @Bean
    Reactor createReactor(Environment env) {
        return Reactors.reactor()
                .env(env)
                .dispatcher(Environment.RING_BUFFER)
                .get();
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
    public BatchingConsumer createBatchingConsumer(){
        return new BatchingConsumer();
    }

    @Bean
    public ReactorMessageConsumer createReactorMessageConsumer(){
        return new ReactorMessageConsumer();
    }

}