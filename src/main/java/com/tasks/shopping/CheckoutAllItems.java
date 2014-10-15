package com.tasks.shopping;


import com.redis.RedisDAO;
import com.redis.RedisDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.function.Function;

import java.util.List;

/**
 * Created by Denys Kovalenko on 10/9/2014.
 */
public class CheckoutAllItems implements Function<List<String>, String> {
    @Autowired
    private RedisDAO redisDAO;


    @Override
    public String apply(List<String> messages) {
        int i = 0;
        for (String message : messages) {
            String echoString = "shopping:checkout";
            redisDAO.save(RedisDAOImpl.RECORD_KEY + (i++), message + ":" + echoString);
        }

        return null;
    }
}
