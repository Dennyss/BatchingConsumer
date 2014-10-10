package com.tasks.shopping.autoparts;

import com.redis.RedisDAO;
import com.redis.RedisDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/8/2014.
 */
public class BuyOil implements Function<String, String> {
    @Autowired
    private RedisDAO redisDAO;

    @Override
    public String apply(String value) {
        String echoString = "autoparts:oil";
        redisDAO.save(RedisDAOImpl.RECORD_KEY, value + ":" + echoString);
        return null;
    }

}
