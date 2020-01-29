package ru.highload.jstests.config;

public class Config {
    // JSON FIELDS
    public static final String PACKAGE_ID_FIELD = "packageId";
    public static final String JS_SCRIPT_FIELD = "jsScript";
    public static final String FUNCTION_NAME_FIELD = "functionName";

    public static final String TESTS_FIELD = "tests";
    public static final String TEST_NAME_FIELD = "testName";
    public static final String EXPECTED_RESULT_FIELD = "expectedResult";
    public static final String PARAMS_FIELD = "params";
    public static final String RESULT_FIELD = "result";
    public static final String CORRECT_FIELD = "correct";
    public static final String ERROR_FIELD = "error";

    // SERVER SETTINGS
    public static final String SERVER_START_OUTPUT = "Server online at http://localhost:8080/\nPress RETURN to stop...";
    public static final String HOST = "localhost";
    public static final int PORT = 8080;

    public static final int TIMEOUT = 5000;

    public static final String TEST_PATH = "test";
    public static final String RESULT_PATH = "result";

    public static final String SUCCESSFUL_TEST_REQUEST_MESSAGE = "Test request accepted";
    public static final String TEST_REQUEST_LOG = "POST on /" + TEST_PATH + ": ";
    public static final String RESULT_REQUEST_LOG = "GET on /" + RESULT_PATH + ": ";

    // ACTORS SETTINGS
    public static final String ACTOR_SYSTEM_NAME = "js-test";

    public static final String ROUTER_ACTOR_NAME = "routerActor";
    public static final String STORAGE_ACTOR_NAME = "storageActor";
    public static final String STORAGE_ACTOR_PATH = "/user/" + ROUTER_ACTOR_NAME + "/" + STORAGE_ACTOR_NAME;

    public static final String NO_TESTS_PERFORMED_RESPONSE = "No tests performed";

    public static final int TEST_ACTORS_COUNT = 5;


    // JS ENGINE SETTINGS
    public static final String JS_ENGINE_NAME = "nashorn";


}
