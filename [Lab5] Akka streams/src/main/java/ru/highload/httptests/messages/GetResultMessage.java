package ru.highload.httptests.messages;

public class GetResultMessage {
    private String URL;
    private Integer count;

    public GetResultMessage(String URL, Integer count) {
        this.URL = URL;
        this.count = count;
    }

    public String getURL() {
        return URL;
    }

    public Integer getCount() {
        return count;
    }
}
