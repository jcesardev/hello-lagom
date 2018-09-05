package io.vetta.hello.lagom.impl;

import akka.Done;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver.Outcome;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

import io.vetta.hello.lagom.impl.HellolagomCommand.Hello;
import io.vetta.hello.lagom.impl.HellolagomCommand.UseGreetingMessage;
import io.vetta.hello.lagom.impl.HellolagomEvent.GreetingMessageChanged;

import static org.junit.Assert.assertEquals;

public class HellolagomEntityTest {
    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("HellolagomEntityTest");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testHellolagomEntity() {
        PersistentEntityTestDriver<HellolagomCommand, HellolagomEvent, HellolagomState> driver = new PersistentEntityTestDriver<>(system,
                new HellolagomEntity(), "world-1");

        Outcome<HellolagomEvent, HellolagomState> outcome1 = driver.run(new Hello("Alice"));
        assertEquals("Hello, Alice!", outcome1.getReplies().get(0));
        assertEquals(Collections.emptyList(), outcome1.issues());

        Outcome<HellolagomEvent, HellolagomState> outcome2 = driver.run(new UseGreetingMessage("Hi"),
                new Hello("Bob"));
        assertEquals(1, outcome2.events().size());
        assertEquals(new GreetingMessageChanged("world-1", "Hi"), outcome2.events().get(0));
        assertEquals("Hi", outcome2.state().message);
        assertEquals(Done.getInstance(), outcome2.getReplies().get(0));
        assertEquals("Hi, Bob!", outcome2.getReplies().get(1));
        assertEquals(2, outcome2.getReplies().size());
        assertEquals(Collections.emptyList(), outcome2.issues());
    }
}
