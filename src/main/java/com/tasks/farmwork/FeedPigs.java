package com.tasks.farmwork;

import com.core.MessageWrapper;
import com.redis.RedisDAO;
import com.redis.RedisDAOImpl;
import com.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/9/2014.
 */
public class FeedPigs implements Function<MessageWrapper, MessageWrapper> {
    @Autowired
    private RedisDAO redisDAO;

    @Override
    public MessageWrapper apply(MessageWrapper value) {
        String echoString = "farm:feedPigs";

        for (int i = 0; i < 20; i++) {
            System.out.println("FeedPigs step executing ...");
            Utils.pause(500);
        }

        redisDAO.save(RedisDAOImpl.RECORD_KEY, value + ":" + echoString);

        return null;
    }

}
