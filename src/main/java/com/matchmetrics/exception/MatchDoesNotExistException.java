package com.matchmetrics.exception;

public class MatchDoesNotExistException extends RuntimeException {

    public MatchDoesNotExistException(int id) {
        super("Match with id " + id + " does not exist.");
    }

}
