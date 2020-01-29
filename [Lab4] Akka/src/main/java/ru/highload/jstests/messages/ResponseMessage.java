package ru.highload.jstests.messages;

import ru.highload.jstests.entities.TestResult;

public class ResponseMessage {
    private TestResult[] testResults;

    public ResponseMessage() {
    }

    public ResponseMessage(TestResultMessage[] testResultMessages) {
        TestResult[] testResultsArray = new TestResult[testResultMessages.length];
        for (int i = 0; i < testResultMessages.length; i++) {
            testResultsArray[i] = testResultMessages[i].getTestResult();
        }
        this.testResults = testResultsArray;
    }

    public TestResult[] getTestResults() {
        return testResults;
    }

    public void setTestResults(TestResult[] testResults) {
        this.testResults = testResults;
    }
}
