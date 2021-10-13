package com.mycompany.app.Message;

import java.util.Observable;

public class Message {
    private final String type;
    private final String value;
    private final String destination;

    public Message(String type, String value, String destination) {
        this.type = type;
        this.value = value;
        this.destination = destination;
    }


    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
