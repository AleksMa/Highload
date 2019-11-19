package ru.highload.jstests.actors;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import ru.highload.jstests.entities.Test;
import ru.highload.jstests.messages.TestPerformMessage;
import ru.highload.jstests.messages.TestResultMessage;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static ru.highload.jstests.config.Config.*;

public class TestActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestPerformMessage.class, msg -> {
                    boolean correct = false;
                    String result = null;
                    String error = null;

                    Test test = msg.getTest();
                    Object[] params = test.getParams();
                    try {
                        ScriptEngine engine = new
                                ScriptEngineManager().getEngineByName(JS_ENGINE_NAME);
                        engine.eval(msg.getJSScript());
                        Invocable invocable = (Invocable) engine;
                        result = invocable.invokeFunction(msg.getFunctionName(), params).toString();
                        correct = result.equals(test.getExpectedResult());
                    } catch (ScriptException e) {
                        System.out.println("Script error: " + e.getMessage());
                        error = e.getMessage();
                    }

                    getContext().actorSelection(STORAGE_ACTOR_PATH)
                            .tell(new TestResultMessage(
                                            test,
                                            msg.getPackageID(),
                                            result,
                                            correct,
                                            error
                                    ),
                                    self());
                })
                .build();
    }
}
