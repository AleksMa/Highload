package ru.highload.anonymizer.server;

import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import ru.highload.anonymizer.messages.GetHostMessage;
import ru.highload.anonymizer.messages.PostHostMessage;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

import static akka.http.javadsl.server.Directives.*;
import static ru.highload.anonymizer.config.Config.*;

public class AnonymizerRouter {

    public Route createRoute(ActorRef storageActor, Http http) {
        return route(
            get(() ->
                parameter(URL_PARAM, requestURL ->
                    parameter(COUNT_PARAM, requestCount -> {
                            int count = Integer.parseInt(requestCount);

                            System.out.println(routerRequestLog(requestURL, count));

                            if (count == 0) {
                                System.out.println(routerSendLog(requestURL));
                                return completeWithFuture(sendRequest(requestURL, http));
                            }

                            return completeWithFuture(Patterns.ask(storageActor, new GetHostMessage(), Duration.ofMillis(TIMEOUT))
                                .thenCompose(msg -> {
                                        PostHostMessage response = (PostHostMessage) msg;
                                        return sendRequest(
                                            fullRequestHostname(response.getHost(), requestURL, count - 1),
                                            http
                                        );
                                    }
                                )
                            );
                        }
                    )
                )
            )
        );
    }

    public CompletionStage<HttpResponse> sendRequest(String requestURL, Http http) {
        return http.singleRequest(HttpRequest.create(requestURL));
    }
}
