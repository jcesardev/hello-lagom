package io.vetta.hello.lagomstream.impl;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import io.vetta.hello.lagom.api.HellolagomService;
import io.vetta.hello.lagomstream.api.HellolagomStreamService;

import javax.inject.Inject;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Implementation of the HellolagomStreamService.
 */
public class HellolagomStreamServiceImpl implements HellolagomStreamService {
    private final HellolagomService hellolagomService;
    private final HellolagomStreamRepository repository;

    @Inject
    public HellolagomStreamServiceImpl(HellolagomService hellolagomService, HellolagomStreamRepository repository) {
        this.hellolagomService = hellolagomService;
        this.repository = repository;
    }

    @Override
    public ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> directStream() {
        return hellos -> completedFuture(
                hellos.mapAsync(8, name -> hellolagomService.hello(name).invoke()));
    }

    @Override
    public ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> autonomousStream() {
        return hellos -> completedFuture(
                hellos.mapAsync(8, name -> repository.getMessage(name).thenApply(message ->
                        String.format("%s, %s!", message.orElse("Hello"), name)
                ))
        );
    }
}
