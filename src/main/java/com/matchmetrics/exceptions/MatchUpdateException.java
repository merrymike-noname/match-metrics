package com.matchmetrics.exceptions;

public class MatchUpdateException extends RuntimeException {

    public MatchUpdateException(int id) {
        super("Match with id " + id + " does not exist.");
    }

}
