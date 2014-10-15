package com.tasks.shopping.grocery;

import com.core.MessageWrapper;
import com.redis.RedisDAO;
import com.redis.RedisDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/7/2014.
 */
public class BuyBananas implements Function<MessageWrapper, MessageWrapper> {
    @Autowired
    private RedisDAO redisDAO;

    @Override
    public MessageWrapper apply(MessageWrapper value) {
        String echoString = "grocery:bananas";
        String derivedValue = value + ":" + echoString;

        redisDAO.save(RedisDAOImpl.RECORD_KEY, derivedValue);

        return MessageWrapper.wrap(derivedValue);
    }
}
