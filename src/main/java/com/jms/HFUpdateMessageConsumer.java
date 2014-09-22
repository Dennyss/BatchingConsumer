package com.jms;

import com.consumer.HFUpdateReactorBatchingConsumer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
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
public class HFUpdateMessageConsumer implements MessageListener {
    private static String messageQueueName = "sm.stateh";
    private static String messageBrokerUrl = "tcp://localhost:61616";
    private Session session;

    private final Processor<Event<String>> writeProcessor;

    public HFUpdateMessageConsumer() {
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

        writeProcessor = new ProcessorSpec<Event<String>>()
                .dataSupplier(new Supplier<Event<String>>() {
                    @Override
                    public Event<String> get() {
                        return new Event<String>(new String());
                    }
                })
                .consume(new HFUpdateReactorBatchingConsumer())
                .get();

    }


    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String text = textMessage.getText();
            //System.out.println("Message received with text: " + text);

            Operation<Event<String>> operation = writeProcessor.get();
            operation.get().setData(text);
            operation.commit();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new HFUpdateMessageConsumer();
    }

}
