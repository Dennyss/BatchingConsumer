package com;

import com.configuration.SpringConfiguration;
import com.jms.JMSMessageProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Denys Kovalenko on 9/19/2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringConfiguration.class)
public class MessageSender {

    @Autowired
    JMSMessageProducer messageProducer;

    @Test
    public void sendMessages() {

        for (int i = 0; i < 10; i++) {
            messageProducer.send("batchProcessing:hello" + i);
        }

    }
}