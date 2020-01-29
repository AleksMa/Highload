package ru.highload.anonymizer.messages;

public class PutHostArrayMessage {
    private String[] hosts;

    public PutHostArrayMessage(String[] hosts) {
        this.hosts = hosts;
    }

    public String[] getHosts() {
        return hosts;
    }

    public void setHosts(String[] hosts) {
        this.hosts = hosts;
    }
}
