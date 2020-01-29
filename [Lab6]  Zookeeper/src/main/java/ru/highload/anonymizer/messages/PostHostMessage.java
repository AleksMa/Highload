package ru.highload.anonymizer.messages;

public class PostHostMessage {
    String host;

    public PostHostMessage(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
