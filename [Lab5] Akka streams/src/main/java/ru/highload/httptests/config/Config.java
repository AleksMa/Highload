package ru.highload.httptests.config;

public class Config {
    public static final long NO_RESULT_TIME = -1L;
    public static final long START_AGGREGATION_TIME = 0L;

    public static final int DEFAULT_PARALLELISM = 1;


    public static final String ACTOR_SYSTEM_NAME = "HTTP-tests";
    public static final String STORAGE_ACTOR_NAME = "storageActor";


    public static final String SERVER_START_OUTPUT = "Server online at http://localhost:8080/\nPress RETURN to stop...";
    public static final String HOST = "localhost";
    public static final int PORT = 8080;

    public static final int OK_STATUS = 200;

    public static final int TIMEOUT = 5000;

    public static final String URL_PARAM = "testURL";
    public static final String URL_PARAM_DEFAULT = "http://mail.ru";

    public static final String COUNT_PARAM = "count";
    public static final String COUNT_PARAM_DEFAULT = "10";

    public static final String MS_PREFIX = " ms";
}
