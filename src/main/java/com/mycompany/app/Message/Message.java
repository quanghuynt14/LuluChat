package com.mycompany.app.Message;

public class Message {
    private String type;
    private String value;
    private String destination;

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

    public String getDestination() {
        return destination;
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
