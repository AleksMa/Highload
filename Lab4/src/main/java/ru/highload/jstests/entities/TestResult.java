package ru.highload.jstests.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import static ru.highload.jstests.config.Config.*;

@JsonPropertyOrder({TEST_NAME_FIELD, CORRECT_FIELD, RESULT_FIELD, ERROR_FIELD, EXPECTED_RESULT_FIELD, PARAMS_FIELD})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestResult {
    private String testName;
    private String expectedResult;
    private Object[] params;

    private String result;
    private boolean correct;
    private String error;

    public TestResult() {
    }

    public TestResult(Test test, String result, boolean correct, String error) {
        this.testName = test.getTestName();
        this.expectedResult = test.getExpectedResult();
        this.params = test.getParams();

        this.result = result;
        this.correct = correct;
        this.error = error;
    }

    public String getTestName() {
        return testName;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public Object[] getParams() {
        return params;
    }

    public String getResult() {
        return result;
    }

    public boolean isCorrect() {
        return correct;
    }

    public String getError() {
        return error;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public void setError(String error) {
        this.error = error;
    }
}
