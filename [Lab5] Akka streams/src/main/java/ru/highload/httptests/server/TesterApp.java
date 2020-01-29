package ru.highload.httptests.server;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.asynchttpclient.AsyncHttpClient;
import ru.highload.httptests.actors.StorageActor;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static ru.highload.httptests.config.Config.*;
import static ru.highload.httptests.server.TesterStreams.createRouteFlow;

public class TesterApp {
    public static void main(String[] args) throws IOException {
        ActorSystem system = ActorSystem.create(ACTOR_SYSTEM_NAME);
        ActorRef storageActorRef = system.actorOf(Props.create(StorageActor.class), STORAGE_ACTOR_NAME);


        final Http http = Http.get(system);
        final AsyncHttpClient HttpClient = asyncHttpClient();
        final ActorMaterializer actorMaterializer = ActorMaterializer.create(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = createRouteFlow(HttpClient, actorMaterializer, storageActorRef);

        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(HOST, PORT),
                actorMaterializer
        );
        System.out.println(SERVER_START_OUTPUT);
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate());
    }
}


// а. Инициализация http сервера в akka

// б. создаем в actorSystem — актор который принимает две команды — поиск уже готового результата тестирования и
// сохранение результата тестрования.

// б. Общая логика требуемого flow
// HttpRequest (этот запрос пришел снаружи) преобразуется в HttpResponse
// Flow.of(HttpRequest.class)
// → map в Pair<url сайта из query параметра, Integer количество запросов>
// → mapAsync,

// С помощью Patterns.ask посылаем запрос в кеширующий актор — есть ли результат. Обрабатываем ответ с помощью метода thenCompose
// если результат уже посчитан, то возвращаем его как completedFuture
// если нет, то создаем на лету flow из данных запроса, выполняем его и возвращаем СompletionStage<Long> :
// Source.from(Collections.singletonList(r))
// .toMat(testSink, Keep.right()).run(materializer);
//
// → map в HttpResponse с результатом а также посылка результата в кеширующий актор.

// в. Общая логика создания внутреннего sink -  testSink
// C помощью метода create создаем Flow
// Flow.<Pair<String, Integer>>create()
//
// →  mapConcat размножаем сообщения до нужного количества копий
// →  mapAsync — засекаем время, вызываем async http client и с помощью метода thenCompose вычисляем время и возвращаем future с временем выполнения запроса
// →  завершаем flow : .toMat(fold, Keep.right() ) ;
// в данном случае fold — это агрегатор который подсчитывает сумму всех времен созаем его с помощью Sink.fold