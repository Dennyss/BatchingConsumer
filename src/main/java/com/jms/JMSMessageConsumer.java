package com.jms;

import com.consumer.ReactorMessageConsumer;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.Reactor;
import reactor.event.Event;
import reactor.event.selector.Selectors;

import javax.jms.*;

/**
 * Created by Denys Kovalenko on 10/6/2014.
 */
public class JMSMessageConsumer implements MessageListener, InitializingBean {
    private static String messageQueueName = "sm.stateh";
    private Session session;

    @Autowired
    private Reactor reactor;

    @Autowired
    private ReactorMessageConsumer reactorMessageConsumer;

    @Override
    public void afterPropertiesSet() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination queue = session.createQueue(messageQueueName);

            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            e.printStackTrace();
        }

        reactor.on(Selectors.$("reactorMessagePattern"), reactorMessageConsumer);
    }


    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            reactor.notify("reactorMessagePattern", Event.wrap(textMessage.getText()));
            //System.out.println(textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}