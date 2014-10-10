package com.jms;

import com.core.InputDispatcher;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.Reactor;
import reactor.event.Event;
import reactor.event.selector.Selectors;
import reactor.function.Consumer;

import javax.jms.*;

/**
 * Created by Denys Kovalenko on 10/6/2014.
 */
public class JMSMessageConsumer implements MessageListener, InitializingBean {
    private Session session;

    @Autowired
    private Reactor reactor;

    @Autowired
    private InputDispatcher inputDispatcher;

    @Override
    public void afterPropertiesSet() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination queue = session.createQueue(JMSMessageProducer.QUEUE_NAME);

            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            e.printStackTrace();
        }

        reactor.on(Selectors.$("hello"), inputDispatcher);
    }


    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            reactor.notify("hello", Event.wrap(textMessage.getText()));
            //System.out.println(textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}