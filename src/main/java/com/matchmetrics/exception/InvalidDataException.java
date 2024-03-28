package com.matchmetrics.exception;

public class InvalidDataException extends IllegalArgumentException {

    public InvalidDataException(String message) { super("Not enough data to perform the operation " + message);}

}
