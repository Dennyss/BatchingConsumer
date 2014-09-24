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
    private static String[] keys = {"key0", "key1", "key2", "key3", "key4"};

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


    // This test must prove that the buffer used there. As an example I set the buffer size 4 messages (it's configurable).
    // So, this test method send messages one by one and checks that no data in DB after each sending.
    // But after sending 5th message (that will fire buffer, with 4 messages to flush) all 4 messages should be in DB.
    @Test
    public void shouldShowBufferBehavior(){
        // Sending 1st message
        String message = keys[0] + DELIMITER + "value" + 0;
        messageProducer.send(message);

        // Check 1st message in DB
        assertNull(redisDao.retrieve(keys[0]));

        // Sending 2nd message
        String message2 = keys[1] + DELIMITER + "value" + 1;
        messageProducer.send(message2);

        // Check 2nd message in DB
        assertNull(redisDao.retrieve(keys[1]));

        // Sending 3d message
        String message3 = keys[2] + DELIMITER + "value" + 2;
        messageProducer.send(message3);

        // Check 3d message in DB
        assertNull(redisDao.retrieve(keys[2]));

        // Sending 4st message
        String message4 = keys[3] + DELIMITER + "value" + 3;
        messageProducer.send(message4);

        // Check 4st message in DB
        assertNull(redisDao.retrieve(keys[3]));

        // Sending 5th message
        String message5 = keys[4] + DELIMITER + "value" + 4;
        messageProducer.send(message5);

        waitWhileDataWillSendToRedis();

        // Check 5th message in DB
        for (int i = 0; i < keys.length - 1; i++) {
            assertEquals("value" + i, redisDao.retrieve(keys[i]));
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
