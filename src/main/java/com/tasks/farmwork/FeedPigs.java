package com.tasks.farmwork;

import com.redis.RedisDAO;
import com.redis.RedisDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.function.Function;

/**
 * Created by Denys Kovalenko on 10/9/2014.
 */
public class FeedPigs implements Function<String, String> {
    @Autowired
    private RedisDAO redisDAO;

    @Override
    public String apply(String value) {
        String echoString = "farm:feedPigs";
        redisDAO.save(RedisDAOImpl.RECORD_KEY, value + ":" + echoString);

        for (int i = 0; i < 20; i++) {
            System.out.println("Plain step 2 executing ...");
            pause(500);
        }

        return null;
    }

    private void pause(long millis) {
        try {
            Thread.currentThread().sleep(millis);
        } catch (InterruptedException e) {
        }
    }

}
