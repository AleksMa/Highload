package ru.highload.jstests.server;

import akka.actor.ActorRef;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import ru.highload.jstests.messages.GetMessage;
import ru.highload.jstests.messages.TestMultiplePerformMessage;
import scala.concurrent.Future;

import static akka.http.javadsl.server.Directives.*;
import static ru.highload.jstests.config.Config.*;

public class HTTPRouter {
    public HTTPRouter() {
    }

    public Route createRoute(ActorRef routerActor) {
        return route(
                path(TEST_PATH, () ->
                        post(() -> entity(
                                Jackson.unmarshaller(TestMultiplePerformMessage.class), msg -> {
                                    System.out.println(TEST_REQUEST_LOG + msg.getPackageID());
                                    routerActor.tell(msg, ActorRef.noSender());
                                    return complete(SUCCESSFUL_TEST_REQUEST_MESSAGE);
                                }
                                )
                        )
                ),
                path(RESULT_PATH, () ->
                        get(() ->
                                parameter(PACKAGE_ID_FIELD, packageID -> {
                                            Future<Object> future = Patterns.ask(routerActor, new GetMessage(packageID), TIMEOUT);
                                            System.out.println(RESULT_REQUEST_LOG + packageID);
                                            return completeOKWithFuture(future, Jackson.marshaller());
                                        }
                                )
                        )
                )
        );
    }
}
