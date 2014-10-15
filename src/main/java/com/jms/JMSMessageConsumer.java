package com.jms;

import com.core.InputDispatcher;
import com.core.LoggingDispatcher;
import com.core.MessageWrapper;
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
    @Autowired
    private Reactor reactor;
    @Autowired
    private InputDispatcher inputDispatcher;
    @Autowired
    private LoggingDispatcher loggingDispatcher;

    @Override
    public void afterPropertiesSet() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination queue = session.createQueue(JMSMessageProducer.QUEUE_NAME);

            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            e.printStackTrace();
        }

        reactor.on(Selectors.$("messageProcessing"), inputDispatcher);
        reactor.on(Selectors.$("logging"), loggingDispatcher);
    }


    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            MessageWrapper messageWrapper = MessageWrapper.wrap(textMessage.getText());
            reactor.notify("messageProcessing", Event.wrap(messageWrapper));
            reactor.notify("logging", Event.wrap(messageWrapper));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}