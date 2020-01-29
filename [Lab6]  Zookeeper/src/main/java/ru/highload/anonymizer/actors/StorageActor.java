package ru.highload.anonymizer.actors;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import ru.highload.anonymizer.messages.GetHostMessage;
import ru.highload.anonymizer.messages.PostHostMessage;
import ru.highload.anonymizer.messages.PutHostArrayMessage;
import scala.util.Random;

public class StorageActor extends AbstractActor {
    private String[] hosts;

    @Override
    public AbstractActor.Receive createReceive() {
        return ReceiveBuilder.create()
            .match(GetHostMessage.class, m -> {
                String randomHost = hosts[new Random().nextInt(hosts.length)];
                sender().tell(new PostHostMessage(randomHost), getSelf());
            })
            .match(PutHostArrayMessage.class, m -> {
                hosts = m.getHosts();
            })
            .build();
    }
}