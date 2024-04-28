package com.matchmetrics.exception;

public class InvalidProbabilityException extends IllegalArgumentException {
    public InvalidProbabilityException(float sum) {
        super("Invalid probability: sum should be equal to 1. Current sum: " + sum);
    }
}
