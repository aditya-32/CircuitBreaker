package org.example;

import com.google.inject.Guice;
import org.example.entity.Request;
import org.example.enums.RequestStatus;
import org.example.handler.CircuitBreaker;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        var injector = Guice.createInjector(new ApplicationModule());
        var circuitBreaker = injector.getInstance(CircuitBreaker.class);
        String rpcName1 = "execute1";
        circuitBreaker.createHandler(rpcName1);
        var currentTime = new Date();
        var r1 = Request.builder()
                .id(1)
                .requestTime(currentTime)
                .requestStatus(RequestStatus.FAIL)
                .build();
        var r2 = Request.builder()
                .id(2)
                .requestTime(new Date(currentTime.toInstant().plus(1, ChronoUnit.SECONDS).toEpochMilli()))
                .requestStatus(RequestStatus.PASS)
                .build();
        var r3 = Request.builder()
                .id(3)
                .requestTime(new Date(currentTime.toInstant().plus(2, ChronoUnit.SECONDS).toEpochMilli()))
                .requestStatus(RequestStatus.FAIL)
                .build();
        var r4 = Request.builder()
                .id(4)
                .requestTime(new Date(currentTime.toInstant().plus(3, ChronoUnit.SECONDS).toEpochMilli()))
                .requestStatus(RequestStatus.FAIL)
                .build();
//        circuitBreaker.sendRequest(rpcName1, r1);
//        circuitBreaker.sendRequest(rpcName1, r2);
//        circuitBreaker.sendRequest(rpcName1, r3);
//        circuitBreaker.sendRequest(rpcName1, r4);
//

        List<Callable<Void>> callebleList = new ArrayList<>();
        try {
            callebleList.add(() -> {
                circuitBreaker.sendRequest(rpcName1, r1);
                return null;
            });
            callebleList.add(() -> {
                circuitBreaker.sendRequest(rpcName1, r2);
                return null;
            });
            callebleList.add(() -> {
                circuitBreaker.sendRequest(rpcName1, r3);
                return null;
            });
            callebleList.add(() -> {
                circuitBreaker.sendRequest(rpcName1, r4);
                return null;
            });
            var executorService = Executors.newCachedThreadPool();
            var futures = executorService.invokeAll(callebleList);
            for (var f : futures) {
                f.get(5, TimeUnit.SECONDS);
            }
        } catch (Exception e) {

        }
    }
}