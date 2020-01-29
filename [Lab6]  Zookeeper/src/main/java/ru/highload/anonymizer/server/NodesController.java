package ru.highload.anonymizer.server;

import akka.actor.ActorRef;
import org.apache.zookeeper.*;
import ru.highload.anonymizer.messages.PutHostArrayMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.highload.anonymizer.config.Config.*;

public class NodesController {
    private ZooKeeper zoo;
    private ActorRef storageActor;

    public NodesController(String ZKHost, ActorRef storageActor) throws IOException {
        this.storageActor = storageActor;
        this.zoo = new ZooKeeper(ZKHost, TIMEOUT, watchedEvent -> {
            System.out.println(watchedEvent.getState());
        });
        watchNodes();
    }

    public void close() throws InterruptedException {
        zoo.close();
    }

    public void addNode(String host, String path, CreateMode mode) throws KeeperException, InterruptedException {
        zoo.create(
            path,
            host.getBytes(),
            ZooDefs.Ids.OPEN_ACL_UNSAFE,
            mode
        );
    }

    public void addServerNode(String host) throws KeeperException, InterruptedException {
        addNode(
            host,
            NODE_ROOT_PATH + NODE_SERVER_PATH,
            CreateMode.EPHEMERAL_SEQUENTIAL
        );
    }

    public void watchNodes() {
        try {
            List<String> nodes = zoo.getChildren(NODE_ROOT_PATH, watchedEvent -> {
                if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                    watchNodes();
                }
            });

            System.out.println(UPDATE_NODE_LOG);

            List<String> hosts = new ArrayList<>();
            for (String node : nodes) {
                System.out.println(node);
                String host = new String(zoo.getData(NODE_ROOT_PATH + "/" + node, false, null));
                hosts.add(host);
            }

            storageActor.tell(new PutHostArrayMessage(hosts.toArray(new String[0])), ActorRef.noSender());

        } catch (KeeperException | InterruptedException e) {
            System.out.println(e.toString());
        }
    }
}
