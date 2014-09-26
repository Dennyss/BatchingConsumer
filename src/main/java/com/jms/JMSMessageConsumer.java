package com.jms;

import com.consumer.HFUpdateBatchingConsumer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.Environment;
import reactor.core.processor.Operation;
import reactor.core.processor.Processor;
import reactor.core.processor.spec.ProcessorSpec;
import reactor.event.Event;
import reactor.function.Supplier;

import javax.jms.*;

/**
 * Created by Denys Kovalenko on 9/19/2014.
 */
@Service("jmsMessageConsumer")
public class JMSMessageConsumer implements MessageListener, InitializingBean {
    private static String messageQueueName = "sm.stateh";
    private static String messageBrokerUrl = "tcp://localhost:61616";
    private Session session;

    @Autowired
    private HFUpdateBatchingConsumer batchingConsumer;

    private Processor<Event<String>> processor;

    @Override
    public void afterPropertiesSet() {
        try {
            BrokerService broker = new BrokerService();
            broker.setPersistent(false);
            broker.setUseJmx(false);
            broker.addConnector(messageBrokerUrl);
            broker.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(messageBrokerUrl);
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

        // Reactor related
        Environment environment = new Environment();

        processor = new ProcessorSpec<Event<String>>()
                .dataSupplier(new Supplier<Event<String>>() {
                    public Event<String> get() {
                        return new Event<String>(new String());
                    }
                })
                .consume(batchingConsumer)
                .get();

    }


    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            Operation<Event<String>> op = processor.prepare();
            op.get().setData(textMessage.getText());
            op.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    // Uncomment it in case we want to run consumer as a standalone app
//    public static void main(String[] args) throws Exception {
//        ApplicationContext context = new ClassPathXmlApplicationContext("application-spring-config.xml");
//    }

}
