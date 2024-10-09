package org.example;

import com.google.inject.AbstractModule;
import com.google.inject.PrivateModule;
import org.example.entity.CircuitBreakerConfig;
import org.example.handler.CircuitBreaker;
import org.example.provider.CircuitBreakerConfigProvider;

import static com.google.inject.Scopes.SINGLETON;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().requireAtInjectOnConstructors();
        binder().requireExactBindingAnnotations();
        binder().requireAtInjectOnConstructors();
        binder().disableCircularProxies();

        bind(CircuitBreakerConfig.class).toProvider(CircuitBreakerConfigProvider.class);
        bind(CircuitBreaker.class).in(SINGLETON);
    }
}
