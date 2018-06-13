package de.jakob.activeMQProducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;

import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.*;
import java.util.Queue;

@RestController
@RequestMapping("/produce/")
public class Producer {

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    Queue queue;

    @GetMapping("/{username}")
    public String publish(@PathVariable("username")
                          final String username) throws JMSException, JsonProcessingException {

        UserObject user = new UserObject();
        user.setName(username);
        user.setAge(45);  // constant age, just for example

        ObjectMapper mapper = new ObjectMapper();
        String userAsString = mapper.writeValueAsString(user);


        jmsTemplate.convertAndSend("my-queue", userAsString);

        return "Produced successfully: " +  userAsString;
    }
}