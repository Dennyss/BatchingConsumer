package com;

import com.configuration.FlowConfiguration;
import com.configuration.SpringConfiguration;
import com.jms.JMSMessageProducer;
import com.redis.RedisDAO;
import com.utils.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        for (int i = 0; i < 10; i++) {
            redisDAO.delete(RedisDAO.RECORD_KEY + i);
        }
    }

    @Test
    public void simpleProcessingShowcase() {
        String message = FlowConfiguration.SIMPLE_SHOPPING_DESTINATION + "#" + "initiate";
        messageProducer.send(message);

        waitWhileDataWillSendToRedis(RedisDAO.RECORD_KEY);

        String result = redisDAO.retrieve(RedisDAO.RECORD_KEY);
        assertEquals(message + ":grocery:apples:grocery:bananas", result);
    }

    @Test
    public void separateProcessingShowcase() {
        String message = FlowConfiguration.SEPARATE_SHOPPING_DESTINATION + "#" + "initiate";
        messageProducer.send(message);

        waitWhileDataWillSendToRedis(RedisDAO.RECORD_KEY);

        String result = redisDAO.retrieve(RedisDAO.RECORD_KEY);
        assertEquals(message + ":grocery:apples:grocery:pepsi:autoparts:tires:autoparts:oil", result);
    }

    @Test
    public void bufferSizeDrivenBatchingProcessingShowcase() {
        String message = FlowConfiguration.BATCHING_SHOPPING_DESTINATION + "#" + "initiate";
        // Send 10 messages
        for (int i = 0; i < 10; i++) {
            messageProducer.send(message);
        }

        // Wait for last record
        waitWhileDataWillSendToRedis(RedisDAO.RECORD_KEY + 9);

        // Check 10 messages
        for (int i = 0; i < 10; i++) {
            String result = redisDAO.retrieve(RedisDAO.RECORD_KEY + i);
            assertEquals(message + ":shopping:putToTheCart:shopping:checkout", result);
        }
    }

    @Test
    public void bufferTimeoutDrivenBatchingProcessingShowcase() {
        String message = FlowConfiguration.BATCHING_SHOPPING_DESTINATION + "#" + "initiate";
        // Send 10 messages
        for (int i = 0; i < 10; i++) {
            messageProducer.send(message);
            Utils.pause(500);
        }

        // Wait for last record
        waitWhileDataWillSendToRedis(RedisDAO.RECORD_KEY + 2);

        // Check first 4 messages (should have value because buffer flush timeout is 2 seconds)
        for (int i = 0; i < 4; i++) {
            String result = redisDAO.retrieve(RedisDAO.RECORD_KEY + i);
            assertEquals(message + ":shopping:putToTheCart:shopping:checkout", result);
        }

        // Check all other messages (should be null because buffer flush timeout is 2 seconds)
        for (int i = 4; i < 10; i++) {
            String result = redisDAO.retrieve(RedisDAO.RECORD_KEY + i);
            assertNull(result);
        }
    }

    @Test
    public void parallelProcessingShowcase() {
        String message = FlowConfiguration.PARALLEL_WORK_DESTINATION + "#" + "initiate";
        messageProducer.send(message);

        waitWhileDataWillSendToRedis(RedisDAO.RECORD_KEY);

        String result = redisDAO.retrieve(RedisDAO.RECORD_KEY);
        assertEquals(message + ":farm:feedChickens:farm:feedPigs", result);
    }

    @Test
    public void batchingProcessingTest() {
        String message = FlowConfiguration.BATCHING_SHOPPING_DESTINATION + "#" + "initiate";
        // Send 10 messages
        for (int i = 0; i < 10; i++) {
            messageProducer.send(message);
        }

        waitWhileDataWillSendToRedis(RedisDAO.RECORD_KEY + 9);

        // Check 10 messages
        for (int i = 0; i < 10; i++) {
            String result = redisDAO.retrieve(RedisDAO.RECORD_KEY + i);
            assertEquals(message + ":shopping:putToTheCart:shopping:checkout", result);
        }

    }


    public void waitWhileDataWillSendToRedis(String dbKey) {
        do {
            Utils.pause(500);
        } while (isDataStillSending(dbKey));
    }

    private boolean isDataStillSending(String dbKey) {
        return redisDAO.retrieve(dbKey) == null;
    }

}
