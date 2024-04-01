package com.matchmetrics.exception;

public class TeamDoesNotExistException extends RuntimeException {
    public TeamDoesNotExistException(String name) {
        super("Team with name '" + name + "' does not exist");
    }
}
