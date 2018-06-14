package de.jakob.activeMQProducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;

import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.web.bind.annotation.*;

import javax.jms.*;
import java.util.Queue;
import java.util.Random;

@RestController
@RequestMapping("/")
public class Producer {

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    Destination addDestination;

    @Autowired
    Destination getRequestDestination;

    @Autowired
    ConnectionFactory connectionFactory;

    @PostMapping("/add")
    ResponseEntity<?> add(@RequestBody UserObject user) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        String userAsString = mapper.writeValueAsString(user);

        jmsTemplate.convertAndSend(addDestination, userAsString);

        return ResponseEntity.ok("Produced successfully: " +  userAsString);
    }


    @GetMapping("/get/{index}")
    public String request(@PathVariable("index")
                          final int index) throws JMSException, JsonProcessingException {

        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination replyDestination = session.createTemporaryQueue();

        Message message = session.createMessage();
        message.setIntProperty("index", index);
        message.setJMSReplyTo(replyDestination);
        message.setJMSCorrelationID(Long.toHexString(new Random(System.currentTimeMillis()).nextLong()));

        jmsTemplate.convertAndSend(getRequestDestination, message);

        MessageConsumer consumer = session.createConsumer(replyDestination);
        TextMessage reply = (TextMessage)consumer.receive();
        System.out.println("RECEIVED: "  + reply.getText());

        session.close();

        return "Received successfully: " +  reply.getText();
    }
}