package io.vetta.hello.lagom.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

import io.vetta.hello.lagom.api.HellolagomService;

/**
 * The module that binds the HellolagomService so that it can be served.
 */
public class HellolagomModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(HellolagomService.class, HellolagomServiceImpl.class);
    }
}
