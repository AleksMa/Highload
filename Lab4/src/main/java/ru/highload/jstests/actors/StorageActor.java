package ru.highload.jstests.actors;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import ru.highload.jstests.messages.GetMessage;
import ru.highload.jstests.messages.ResponseMessage;
import ru.highload.jstests.entities.Test;
import ru.highload.jstests.messages.TestResultMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static ru.highload.jstests.config.Config.NO_TESTS_PERFORMED_RESPONSE;

public class StorageActor extends AbstractActor {
    private Map<String, ArrayList<TestResultMessage>> storage = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestResultMessage.class, msg -> {
                    if (!storage.containsKey(msg.getPackageID())) {
                        ArrayList<TestResultMessage> initList = new ArrayList<>();
                        initList.add(msg);
                        storage.put(msg.getPackageID(), initList);
                    } else {
                        storage.get(msg.getPackageID()).add(msg);
                    }
                })
                .match(GetMessage.class, msg -> {
                    ArrayList<TestResultMessage> responseList = storage.get(msg.getPackageID());
                    if (responseList != null) {
                        TestResultMessage[] responseArray = new TestResultMessage[responseList.size()];
                        responseList.toArray(responseArray);
                        ResponseMessage responseMessage = new ResponseMessage(responseArray);

                        sender().tell(responseMessage, self());
                    } else {
                        sender().tell(NO_TESTS_PERFORMED_RESPONSE, self());
                    }
                })
                .build();
    }
}
