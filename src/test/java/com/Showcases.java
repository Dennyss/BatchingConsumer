package com;

import com.configuration.FlowConfiguration;
import com.configuration.SpringConfiguration;
import com.jms.JMSMessageProducer;
import com.redis.RedisDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by Denys Kovalenko on 10/9/2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringConfiguration.class)
public class Showcases {
    @Autowired
    private RedisDAO redisDAO;

    @Autowired
    private JMSMessageProducer messageProducer;

    @Before
    public void cleanDB() throws Exception {
        redisDAO.delete(RedisDAO.RECORD_KEY);
    }

    @Test
    public void separateProcessingShowcase() {
        String message = FlowConfiguration.SEPARATE_SHOPPING_DESTINATION + "#" + "initiate";
        messageProducer.send(message);
        waitWhileDataWillSendToRedis();
        String result = redisDAO.retrieve(RedisDAO.RECORD_KEY);
        assertEquals(message + ":grocery:apples:grocery:bananas:autoparts:tires:autoparts:oil", result);
    }




    public void waitWhileDataWillSendToRedis() {
        do {
            pause(500);
        } while (isDataStillSending());
    }

    private boolean isDataStillSending() {
        return redisDAO.retrieve(RedisDAO.RECORD_KEY) == null;
    }

    private void pause(long millis) {
        try {
            Thread.currentThread().sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
