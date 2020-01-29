package ru.highload.jstests.server;

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
import ru.highload.jstests.actors.RouterActor;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

import static ru.highload.jstests.config.Config.*;

public class JSTester {
    public static void main(String[] args) throws IOException {
        ActorSystem system = ActorSystem.create(ACTOR_SYSTEM_NAME);
        ActorRef routerActor = system.actorOf(Props.create(RouterActor.class), ROUTER_ACTOR_NAME);

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        HTTPRouter instance = new HTTPRouter();

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow =
                instance.createRoute(routerActor).flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(HOST, PORT),
                materializer
        );
        System.out.println(SERVER_START_OUTPUT);
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate());
    }
}


//TASK_DONE
// а. Создаем actor system

//TASK_DONE
// б. В приложении будем использовать следующие акторы :
//    - актор который хранит результаты тестов.
//    Обрабатывает следующие сообщения :
//    cообщение с результатом одного теста -> кладет его в локальное хранилище.
//    Сообщение с запросом результата теста → отвечает сообщением с
//    результатом всех тестов для заданного packageId
//    - актор который исполняет один тест из пакета.
//    Для исполнения JS кода можно воспользоваться следующим примером
//    ScriptEngine engine = new
//    ScriptEngineManager().getEngineByName("nashorn");
//    engine.eval(jscript);
//    Invocable invocable = (Invocable) engine;
//    return invocable.invokeFunction(functionName, params).toString();
//    После исполнения теста результат передается актору хранилищу
//    - актор роутер
//    инициализирует актор хранилище а также пул акторов исполнителей тестов

//TASK_DONE
// в. После инициализации actor system — создаем актор роутер который в свою
//    очередь создает все дочерние акторы

//TASK_DONE
// г. Создаем ActorMaterializer и инициализируем http систему с помощью
//    high level api

//TASK_DONE
// д. Cтроим дерево route и пишем обработчики запросов

//TASK_DONE
// е. Когда приходит запрос на запуск теста — запускаем тест и сразу
//    овтечаем константным ответом.

//TASK_DONE
// ё. В случае запроса на получение информции о тесте — используем
//    Putterns.ask и возвращаем Future с ответом
