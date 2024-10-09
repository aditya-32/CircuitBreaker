package org.example.exceptions;

public class CircuitBreakerExceptions extends RuntimeException{
    public CircuitBreakerExceptions(String msg) {
        super(msg);
    }
}
