package ru.highload.jstests.messages;

import ru.highload.jstests.entities.Test;

public class TestPerformMessage {
    private String packageID;
    private String JSScript;
    private String functionName;

    private Test test;

    public TestPerformMessage() {
    }

    public TestPerformMessage(Test test, String packageID, String JSScript, String functionName) {
        this.test = test;
        this.packageID = packageID;
        this.JSScript = JSScript;
        this.functionName = functionName;
    }

    public Test getTest() {
        return test;
    }

    public String getPackageID() {
        return packageID;
    }

    public String getJSScript() {
        return JSScript;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}
