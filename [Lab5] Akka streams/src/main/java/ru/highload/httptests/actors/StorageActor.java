package ru.highload.httptests.actors;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import ru.highload.httptests.messages.GetResultMessage;
import ru.highload.httptests.messages.PutResultMessage;
import ru.highload.httptests.messages.ResponseResultMessage;

import java.util.HashMap;
import java.util.Map;

import static ru.highload.httptests.config.Config.NO_RESULT_TIME;


public class StorageActor extends AbstractActor {
    private Map<String, Long> storage = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(PutResultMessage.class, msg -> {
                    storage.put(msg.getURL(), msg.getTime());
                })
                .match(GetResultMessage.class, msg -> {
                    boolean evaluated = false;
                    Long resultTime = NO_RESULT_TIME;

                    if (storage.containsKey(msg.getURL())) {
                        evaluated = true;
                        resultTime = storage.get(msg.getURL());
                    }
                    sender().tell(new ResponseResultMessage(msg.getURL(), resultTime, evaluated), self());
                })
                .build();
    }
}
