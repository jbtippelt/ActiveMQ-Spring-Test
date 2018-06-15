package de.jakob.activeMQConsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Consumer {

    @Autowired
    ConnectionFactory connectionFactory;

    @Autowired
    JmsTemplate jmsTemplate;

    private List<UserObject> list = new ArrayList<>();
    private static int index = 0;

    @JmsListener(destination = "add-user-queue")
    public void add(String userAsString) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        UserObject user = mapper.readValue(userAsString, UserObject.class);

        list.add(index, user);

        System.out.println("Added user: " + user.getName() + ", " + user.getAge() + " at index " + index);
        index++;
    }

    @JmsListener(destination = "get-user-queue")
    public void receiveReply(Message indexMessage) throws IOException, JMSException {
        System.out.println("Received Message: " + indexMessage);

        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
        ObjectMapper mapper = new ObjectMapper();
        int i = indexMessage.getIntProperty("index");

        String replyPayload = "No user with this index.";

        try {
            UserObject user = list.get(i);
            replyPayload = mapper.writeValueAsString(user);

        } catch (Exception e) {
            System.out.println("ERROR: " + e);
        }

        TextMessage replyMessage = session.createTextMessage(replyPayload);
        replyMessage.setJMSDestination(indexMessage.getJMSReplyTo());
        replyMessage.setJMSCorrelationID(indexMessage.getJMSCorrelationID());

        MessageProducer producer = session.createProducer(indexMessage.getJMSReplyTo());
        producer.send(replyMessage);

        session.close();

        System.out.println("Replyed Message: " + replyMessage);

    }
}