package ru.highload.jstests.messages;

import ru.highload.jstests.entities.Test;
import ru.highload.jstests.entities.TestResult;

public class TestResultMessage {
    private TestResult testResult;

    private String packageID;

    public TestResultMessage() {
    }

    public TestResultMessage(Test test, String packageID, String result, boolean correct, String error) {
        this.testResult = new TestResult(test, result, correct, error);
        this.packageID = packageID;
    }

    public TestResult getTestResult() {
        return testResult;
    }

    public String getPackageID() {
        return packageID;
    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }

    public void setPackageID(String packageID) {
        this.packageID = packageID;
    }
}
