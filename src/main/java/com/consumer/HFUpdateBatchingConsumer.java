package com.consumer;

import com.redis.RedisDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by Denys Kovalenko on 9/19/2014.
 */
@Service
public class HFUpdateBatchingConsumer extends AbstractBatchingConsumer {

    @Autowired
    private RedisDAOImpl redisDAO;

    @Override
    public void process(List<String> messages) {
        redisDAO.saveAll(messages);
    }
}
