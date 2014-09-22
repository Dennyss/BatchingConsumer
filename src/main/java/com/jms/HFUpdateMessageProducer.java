package com.jms;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Random;

/**
 * Created by Denys Kovalenko on 9/19/2014.
 */
public class HFUpdateMessageProducer {
    private static String clientQueueName = "sm.stateh";
    private Session session;
    private MessageProducer producer;

    public HFUpdateMessageProducer() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination queue = session.createQueue(clientQueueName);

            producer = session.createProducer(queue);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void send(String messageText){
        try {
            TextMessage txtMessage = session.createTextMessage();
            txtMessage.setText(messageText);
            txtMessage.setJMSCorrelationID(createRandomString());
            producer.send(txtMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private String createRandomString() {
        Random random = new Random(System.currentTimeMillis());
        long randomLong = random.nextLong();
        return Long.toHexString(randomLong);
    }
}
