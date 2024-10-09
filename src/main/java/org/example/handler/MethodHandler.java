package org.example.handler;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.CircuitBreakerConfig;
import org.example.entity.Method;
import org.example.entity.Request;
import org.example.exceptions.CircuitBreakerExceptions;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MethodHandler {
    @Assisted
    private final Method method;
    private final CircuitBreakerConfig config;
    private final ReentrantLock lock;

    @Inject
    public MethodHandler(@Assisted Method method, CircuitBreakerConfig config) {
        this.method = method;
        this.config = config;
        this.lock = new ReentrantLock();
    }

    public void addRequest(Request request) {
        lock.lock();
        if (method.isCircuitOpen()) {
            System.out.println("Request dropped "+request.getId());
            throw new CircuitBreakerExceptions("Circuit is open dropping request "+request.getId());
        }
        method.addRequest(request);
        checkCircuitBreakerStatus();
        lock.unlock();
        System.out.println("Request Processed "+request.getId());
    }
    private void checkCircuitBreakerStatus() {
        var requests = method.getRequests();
        if (requests.size() < config.getMinRequests()) {
            return;
        }
        System.out.println("Request size "+requests.size()+" current time "+ System.nanoTime());
        var lastValidRequest = new Date(Instant.now().minus(config.getTimeWindowSec(), ChronoUnit.SECONDS).toEpochMilli());
        while(!requests.isEmpty() &&
                requests.peek().getRequestTime().before(lastValidRequest)) {
            requests.poll();
        }
        var failurePer = method.getFailurePercentage();
        System.out.println("Failure %"+failurePer);
        if (failurePer >= config.getFailureRatioThreshold()) {

            method.openCircuit();
            new Thread(() -> {
                try {
                    Thread.sleep(config.getCircuitCloseTimeSec()*1000);
                    method.closeCircuit();
                } catch (Exception e) {
                    log.info("Thread Interrupted");
                }
            }).start();
        }
    }
}
