package org.example.handler;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.CircuitBreakerConfig;
import org.example.entity.Method;
import org.example.entity.Request;
import org.example.exceptions.CircuitBreakerExceptions;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CircuitBreaker {
    private static final Map<String, MethodHandler> handlerMap = new HashMap<>();

    private final CircuitBreakerConfig config;

    public void createHandler(String rpcName) throws CircuitBreakerExceptions {
        if (handlerMap.containsKey(rpcName)) {
            throw new CircuitBreakerExceptions("Handler Already Registered");
        }
        var method = Method.builder().name(rpcName).build();
        var handler = new MethodHandler(method, config);
        handlerMap.put(rpcName, handler);
    }

    public void sendRequest(String rpcName, Request request) {
        if (!handlerMap.containsKey(rpcName)) {
            System.out.println("No Handler Registered for "+rpcName);
        }
//        System.out.println("Sending Sent "+request.getId());
        var handler = handlerMap.get(rpcName);
        handler.addRequest(request);
    }
}
