package ru.highload.anonymizer.config;

public class Config {
    public static final String ACTOR_SYSTEM_NAME = "anonimizerSystem";
    public static final String STORAGE_ACTOR_NAME = "storageActor";

    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 8081;
    public static final String ZOOKEEPER_HOST = "localhost:2181";

    public static final int TIMEOUT = 5000;

    public static final String URL_PARAM = "url";
    public static final String COUNT_PARAM = "count";

    public static final String NODE_ROOT_PATH = "/servers";
    public static final String NODE_SERVER_PATH = "/s";

    public static final String UPDATE_NODE_LOG = "Child nodes changed. Actual nodes: ";


    public static String startedLog(String host, int port) {
        return "Server online at http://" + host + ":" + port;
    }

    public static String routerRequestLog(String requestURL, int count) {
        return requestURL + " : " + count;
    }

    public static String routerSendLog(String requestURL) {
        return "Request send to " + requestURL;
    }

    public static String fullServerHostname(String host, int port) {
        return "http://" + host + ":" + port;
    }

    public static String fullRequestHostname(String host, String requestURL, int count) {
        return host + "/?url=" + requestURL + "&count=" + count;
    }
}
