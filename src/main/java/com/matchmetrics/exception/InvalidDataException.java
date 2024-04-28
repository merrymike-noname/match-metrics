package com.matchmetrics.exception;

public class InvalidDataException extends IllegalArgumentException {

    public InvalidDataException(String message) { super("The input data is invalid: " + message);}

}
