package ru.highload.httptests.messages;

public class ResponseResultMessage {
    private String URL;

    private boolean stored;
    private Long time;

    public ResponseResultMessage(String URL, Long time, boolean stored) {
        this.URL = URL;
        this.stored = stored;
        this.time = time;
    }

    public String getURL() {
        return URL;
    }

    public Long getTime() {
        return time;
    }

    public boolean isStored() {
        return stored;
    }
}
