package com;

import com.jms.JMSMessageProducer;
import com.redis.RedisDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.redis.RedisDAOImpl.*;
import static org.junit.Assert.*;

/**
 * Created by Denys Kovalenko on 9/23/2014.
 */
@ContextConfiguration(locations = {"classpath:application-spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class BatchingConsumerTest {
    private static String[] keys = {"key1", "key2", "key3", "key4", "key5"};

    @Autowired
    JMSMessageProducer messageProducer;

    @Autowired
    private RedisDAO redisDao;

    @Before
    public void cleanDB() throws Exception {
        for (String key : keys) {
            redisDao.delete(key);
        }
    }

    @Test
    public void shouldNotStoreOneMessage() {
        String message = keys[0] + DELIMITER + "value";
        messageProducer.send(message);

        String actualResult = redisDao.retrieve(keys[0]);
        // Because buffer size is 4 messages, 1 message will never perform buffer to flush, so no data in DB
        assertNull(actualResult);
    }

    @Test
    public void shouldSend5ButProceed4() {
        // Send 5 messages one by one
        for (int i = 0; i < keys.length; i++) {
            String message = keys[i] + DELIMITER + "value" + i;
            messageProducer.send(message);
        }

        waitWhileDataWillSendToRedis();

        // Check that 4 messages were stores in DB, one left in buffer
        for (int i = 0; i < keys.length - 1; i++) {
            String actualResult = redisDao.retrieve(keys[i]);
            assertEquals("value" + i, actualResult);
        }
    }

    private void waitWhileDataWillSendToRedis() {
        do {
            pause(500);
        } while (isDataStillSending());
    }

    private boolean isDataStillSending() {
        return redisDao.retrieve(keys[0]) == null;
    }

    private void pause(long millis) {
        try {
            Thread.currentThread().sleep(millis);
        } catch (InterruptedException e) {
        }
    }

}
