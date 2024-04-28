package com.matchmetrics.exception;

public class ProbabilityDoesNotExistException extends RuntimeException {

    public ProbabilityDoesNotExistException(int id) {
        super("Probability with id '" + id + "' does not exist.");
    }

}