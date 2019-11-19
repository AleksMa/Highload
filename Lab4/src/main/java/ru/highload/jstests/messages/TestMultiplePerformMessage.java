package ru.highload.jstests.messages;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.highload.jstests.entities.Test;

import static ru.highload.jstests.config.Config.*;

@JsonAutoDetect
public class TestMultiplePerformMessage {
    @JsonProperty(PACKAGE_ID_FIELD)
    private String packageID;
    @JsonProperty(JS_SCRIPT_FIELD)
    private String JSScript;
    @JsonProperty(FUNCTION_NAME_FIELD)
    private String functionName;

    @JsonProperty(TESTS_FIELD)
    private Test[] tests;

    public TestMultiplePerformMessage() {
    }

    public TestMultiplePerformMessage(Test[] tests, String packageID, String JSScript, String functionName) {
        this.tests = tests;
        this.packageID = packageID;
        this.JSScript = JSScript;
        this.functionName = functionName;
    }

    public Test[] getTests() {
        return tests;
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

    public void setTests(Test[] tests) {
        this.tests = tests;
    }
}
