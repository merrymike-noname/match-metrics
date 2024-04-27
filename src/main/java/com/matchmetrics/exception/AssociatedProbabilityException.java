package com.matchmetrics.exception;

public class AssociatedProbabilityException extends RuntimeException {
    public AssociatedProbabilityException(int id) {
        super("Cannot delete associated probability with id '" + id + "'.");
    }
}
