package de.jakob.activeMQProducer;

import java.io.Serializable;

public class UserObject {

    private String name;
    private int age;

    // Getter and Setter ...
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
