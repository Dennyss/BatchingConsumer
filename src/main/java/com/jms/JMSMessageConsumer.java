package com.jms;

import com.consumer.HFUpdateBatchingConsumer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.Environment;
import reactor.core.composable.Deferred;
import reactor.core.composable.Stream;
import reactor.core.composable.spec.Streams;

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

    //    private Processor<Event<String>> writeProcessor;
    private Deferred<String, Stream<String>> deferred;

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

//        writeProcessor = new ProcessorSpec<Event<String>>()
//                .dataSupplier(new Supplier<Event<String>>() {
//                    @Override
//                    public Event<String> get() {
//                        return new Event<String>(new String());
//                    }
//                })
//                .consume(batchingConsumer)
//                .get();

        deferred = Streams.<String>defer()
                .env(environment)
                .dispatcher(Environment.RING_BUFFER)
                .get();

        Stream<String> stream = deferred.compose();
        stream.consume(batchingConsumer);
    }


    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
//            Operation<Event<String>> operation = writeProcessor.get();
//            operation.get().setData(textMessage.getText());
//            operation.commit();

            deferred.accept(textMessage.getText());

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    // Uncomment it in case we want to run consumer as a standalone app
//    public static void main(String[] args) throws Exception {
//        ApplicationContext context = new ClassPathXmlApplicationContext("application-spring-config.xml");
//    }

}
