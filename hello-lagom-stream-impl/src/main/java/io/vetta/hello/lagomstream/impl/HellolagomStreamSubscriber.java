package io.vetta.hello.lagomstream.impl;

import akka.Done;
import akka.stream.javadsl.Flow;

import io.vetta.hello.lagom.api.HellolagomEvent;
import io.vetta.hello.lagom.api.HellolagomService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * This subscribes to the HellolagomService event stream.
 */
public class HellolagomStreamSubscriber {
    @Inject
    public HellolagomStreamSubscriber(HellolagomService hellolagomService, HellolagomStreamRepository repository) {
        // Create a subscriber
        hellolagomService.helloEvents().subscribe()
                // And subscribe to it with at least once processing semantics.
                .atLeastOnce(
                        // Create a flow that emits a Done for each message it processes
                        Flow.<HellolagomEvent>create().mapAsync(1, event -> {
                            if (event instanceof HellolagomEvent.GreetingMessageChanged) {
                                HellolagomEvent.GreetingMessageChanged messageChanged = (HellolagomEvent.GreetingMessageChanged) event;
                                // Update the message
                                return repository.updateMessage(messageChanged.getName(), messageChanged.getMessage());
                            } else {
                                // Ignore all other events
                                return CompletableFuture.completedFuture(Done.getInstance());
                            }
                        })
                );
    }
}
