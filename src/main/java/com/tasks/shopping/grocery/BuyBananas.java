package com.tasks.shopping.grocery;

import com.redis.RedisDAO;
import com.redis.RedisDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/7/2014.
 */
public class BuyBananas implements Function<String, String> {
    @Autowired
    private RedisDAO redisDAO;

    @Override
    public String apply(String value) {
        String echoString = "grocery:bananas";
        String derivedValue = value + ":" + echoString;

        redisDAO.save(RedisDAOImpl.RECORD_KEY, derivedValue);

        return derivedValue;
    }
}
