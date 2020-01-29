package ru.highload.httptests.messages;

public class PutResultMessage {
    private String URL;
    private Long time;

    public PutResultMessage(String URL, Long time) {
        this.URL = URL;
        this.time = time;
    }

    public String getURL() {
        return URL;
    }

    public Long getTime() {
        return time;
    }
}
