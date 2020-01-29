package ru.highload.anonymizer.server;

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
import org.apache.zookeeper.KeeperException;
import ru.highload.anonymizer.actors.StorageActor;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

import static ru.highload.anonymizer.config.Config.*;

public class AnonymizerApp {

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        if (args.length > 0) {
            host = args[0];
        }
        if (args.length > 1) {
            port = Integer.parseInt(args[1]);
        }

        final ActorSystem system = ActorSystem.create(ACTOR_SYSTEM_NAME);
        final ActorRef storageActor = system.actorOf(Props.create(StorageActor.class), STORAGE_ACTOR_NAME);

        final ActorMaterializer actorMaterializer = ActorMaterializer.create(system);
        final Http http = Http.get(system);
        final AnonymizerRouter router = new AnonymizerRouter();

        final NodesController controller = new NodesController(ZOOKEEPER_HOST, storageActor);
        controller.addServerNode(fullServerHostname(host, port));

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = router.createRoute(storageActor, http)
            .flow(system, actorMaterializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
            routeFlow,
            ConnectHttp.toHost(host, port),
            actorMaterializer
        );

        System.out.println(startedLog(host, port));
        System.in.read();
        controller.close();
        binding
            .thenCompose(ServerBinding::unbind)
            .thenAccept(unbound -> system.terminate());
    }

}


// а. установка zookeeper
//https://zookeeper.apache.org/doc/r3.5.5/zookeeperStarted.html#sc_InstallingSingleMode

//а. создаем актор хранилище конфигурации.
//Он принимает две команды —
//-	список серверов (который отправит zookeeper watcher)
//-	запрос на получение случайного сервера

//б. создаем с помощью api route в акка http сервер который принимает два параметра, и если счетчик не равен 0,
// то сначала получает новый урл сервера (от актора хранилища конфигурации) и делает запрос к нему
// с аналогичными query параметрами (url, counter) но счетчиком на 1 меньше. Либо осуществляет  запрос по url из параметра


//в. в акка встроен http клиент
//пример его вызова -
//
//final Http http = Http.get(context().system());
//
//CompletionStage<HttpResponse> fetch(String url) {
//    return http.singleRequest(HttpRequest.create(url));
//  }
