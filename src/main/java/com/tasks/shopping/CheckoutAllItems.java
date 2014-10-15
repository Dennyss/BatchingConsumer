package com.tasks.shopping;


import com.core.MessageWrapper;
import com.redis.RedisDAO;
import com.redis.RedisDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.function.Function;

import java.util.List;

/**
 * Created by Denys Kovalenko on 10/9/2014.
 */
public class CheckoutAllItems implements Function<List<MessageWrapper>, MessageWrapper> {
    @Autowired
    private RedisDAO redisDAO;


    @Override
    public MessageWrapper apply(List<MessageWrapper> messages) {
        int i = 0;
        for (MessageWrapper message : messages) {
            String echoString = "shopping:checkout";
            redisDAO.save(RedisDAOImpl.RECORD_KEY + (i++), message + ":" + echoString);
        }

        return null;
    }
}
