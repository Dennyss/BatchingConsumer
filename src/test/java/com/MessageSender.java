package com;

import com.jms.HFUpdateMessageProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Denys Kovalenko on 9/19/2014.
 */
@ContextConfiguration(locations = {"classpath:test-spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class MessageSender {

    @Test
    public void sendMessages() {
        HFUpdateMessageProducer messageProducer = new HFUpdateMessageProducer();

        for (int i = 0; i < 10; i++) {
            messageProducer.send("hello" + i);
        }

    }
}
