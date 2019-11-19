package ru.highload.jstests.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.routing.RoundRobinPool;
import ru.highload.jstests.messages.GetMessage;
import ru.highload.jstests.entities.Test;
import ru.highload.jstests.messages.TestMultiplePerformMessage;
import ru.highload.jstests.messages.TestPerformMessage;

import static ru.highload.jstests.config.Config.*;

public class RouterActor extends AbstractActor {
    private ActorRef storageActor;
    private ActorRef testActorPool;

    RouterActor() {
        this.storageActor = getContext().actorOf(Props.create(StorageActor.class), STORAGE_ACTOR_NAME);
        this.testActorPool = getContext().actorOf(new RoundRobinPool(TEST_ACTORS_COUNT).props(Props.create(TestActor.class)));
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestMultiplePerformMessage.class, msg -> {
                    for (Test test : msg.getTests()) {
                        testActorPool.tell(new TestPerformMessage(
                                test,
                                msg.getPackageID(),
                                msg.getJSScript(),
                                msg.getFunctionName()
                        ), self());
                    }
                })
                .match(GetMessage.class, msg ->
                        storageActor.tell(msg, sender())
                )
                .build();
    }
}
