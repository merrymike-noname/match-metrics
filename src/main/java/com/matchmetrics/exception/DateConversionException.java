package com.matchmetrics.exception;

public class DateConversionException extends RuntimeException {

    public DateConversionException(Throwable cause) {
        super("Error converting string to date: " + cause.getLocalizedMessage());
    }

    public DateConversionException(String message) {
        super("Error converting string to date: " + message);
    }

}
