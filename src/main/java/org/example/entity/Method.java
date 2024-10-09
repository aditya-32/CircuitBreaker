package org.example.entity;

import lombok.Builder;
import lombok.Getter;
import org.example.enums.RequestStatus;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Builder
public class Method {
    private String name;
    @Builder.Default
    private Queue<Request> requests = new ArrayDeque<>();
    @Builder.Default
    private AtomicBoolean isCircuitOpen = new AtomicBoolean(false);

    public void openCircuit() {
        this.isCircuitOpen.compareAndSet(false, true);
    }

    public void closeCircuit() {
        this.isCircuitOpen.compareAndSet(true, false);
    }

    public boolean isCircuitOpen() {
        return isCircuitOpen.get();
    }

    public void addRequest(Request request) {
        this.requests.add(request);
    }

    public Double getFailurePercentage() {
        var failedRequest = requests.stream()
                .filter(request -> RequestStatus.FAIL.equals(request.getRequestStatus()))
                .toList();
        var successRequest = requests.stream()
                .filter(request -> RequestStatus.PASS.equals(request.getRequestStatus()))
                .toList();
        return (double) ((failedRequest.size())*100/(failedRequest.size() + successRequest.size()));
    }
}
