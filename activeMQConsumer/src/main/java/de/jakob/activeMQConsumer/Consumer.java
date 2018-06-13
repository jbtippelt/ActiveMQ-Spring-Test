package de.jakob.activeMQConsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Consumer {

    @JmsListener(destination = "my-queue")
    public void receive(String userAsString) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        UserObject user = mapper.readValue(userAsString, UserObject.class);

        System.out.println("Received user: " + user.getName() + ", " + user.getAge());
    }
}