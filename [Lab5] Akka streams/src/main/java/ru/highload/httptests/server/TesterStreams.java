package ru.highload.httptests.server;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import org.asynchttpclient.AsyncHttpClient;
import ru.highload.httptests.messages.GetResultMessage;
import ru.highload.httptests.messages.PutResultMessage;
import ru.highload.httptests.messages.ResponseResultMessage;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static ru.highload.httptests.config.Config.*;

public class TesterStreams {
    static Flow<HttpRequest, HttpResponse, NotUsed> createRouteFlow(AsyncHttpClient HttpClient,
                                                                    ActorMaterializer actorMaterializer,
                                                                    ActorRef storageActorRef) {
        return Flow.of(HttpRequest.class)
                .map(request -> {
                    String URL = requestHelper(request, URL_PARAM, URL_PARAM_DEFAULT);
                    int count = Integer.parseInt(requestHelper(request, COUNT_PARAM, COUNT_PARAM_DEFAULT));

                    return new GetResultMessage(URL, count);
                })
                .mapAsync(DEFAULT_PARALLELISM, getMessage ->
                        Patterns.ask(storageActorRef, getMessage, Duration.ofMillis(TIMEOUT))
                                .thenCompose(msg -> {
                                    ResponseResultMessage responseMessage = (ResponseResultMessage) msg;

                                    if (responseMessage.isStored()) {
                                        return CompletableFuture.completedFuture(responseMessage);
                                    }
                                    return Source
                                            .from(Collections.singletonList(getMessage))
                                            .toMat(
                                                    createRouteSink(
                                                            HttpClient,
                                                            getMessage.getCount()
                                                    ),
                                                    Keep.right()
                                            )
                                            .run(actorMaterializer)
                                            .thenCompose(time ->
                                                    CompletableFuture.completedFuture(
                                                            new ResponseResultMessage(
                                                                    getMessage.getURL(),
                                                                    time / getMessage.getCount(),
                                                                    false
                                                            )));
                                }))
                .map(responseMessage -> {
                    if (!responseMessage.isStored()) {
                        PutResultMessage putMessage = new PutResultMessage(
                                responseMessage.getURL(),
                                responseMessage.getTime()
                        );
                        storageActorRef.tell(putMessage, ActorRef.noSender());
                    }
                    return HttpResponse.create().withStatus(OK_STATUS).withEntity(responseMessage.getTime() + MS_PREFIX);
                });
    }


    private static Sink<GetResultMessage, CompletionStage<Long>> createRouteSink(AsyncHttpClient HttpClient,
                                                                                 int parallelism) {
        return Flow.<GetResultMessage>create()
                .mapConcat(msg -> Collections.nCopies(
                        msg.getCount(),
                        msg.getURL()))
                .mapAsync(parallelism, URL -> {
                    long start = System.currentTimeMillis();

                    return HttpClient
                            .prepareGet(URL)
                            .execute()
                            .toCompletableFuture()
                            .thenCompose(response ->
                                    CompletableFuture.completedFuture(System.currentTimeMillis() - start));
                })
                .toMat(Sink.fold(START_AGGREGATION_TIME, Long::sum), Keep.right());
    }

    private static String requestHelper(HttpRequest request, String param, String defaultValue) {
        return request.getUri().query().getOrElse(param, defaultValue);
    }
}
