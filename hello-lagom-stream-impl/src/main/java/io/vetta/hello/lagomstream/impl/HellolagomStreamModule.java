package io.vetta.hello.lagomstream.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

import io.vetta.hello.lagom.api.HellolagomService;
import io.vetta.hello.lagomstream.api.HellolagomStreamService;

/**
 * The module that binds the HellolagomStreamService so that it can be served.
 */
public class HellolagomStreamModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        // Bind the HellolagomStreamService service
        bindService(HellolagomStreamService.class, HellolagomStreamServiceImpl.class);
        // Bind the HellolagomService client
        bindClient(HellolagomService.class);
        // Bind the subscriber eagerly to ensure it starts up
        bind(HellolagomStreamSubscriber.class).asEagerSingleton();
    }
}
