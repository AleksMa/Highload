package ru.highload.jstests.messages;

public class GetMessage {
    private String packageID;

    public GetMessage() {
    }

    public GetMessage(String packageID) {
        this.packageID = packageID;
    }

    public String getPackageID() {
        return packageID;
    }
}
