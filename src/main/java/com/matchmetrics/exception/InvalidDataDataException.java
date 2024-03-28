package com.matchmetrics.exception;

public class InvalidDataDataException extends IllegalArgumentException {

    public InvalidDataDataException(String message) { super("Not enough data to perform the operation " + message);}

}
