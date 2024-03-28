package com.matchmetrics.exception;

public class NotEnoughDataException extends IllegalArgumentException {
    public NotEnoughDataException(String message) {
        super("Not enough data to perform the operation " + message);
    }
}
