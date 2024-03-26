package com.matchmetrics.exceptions;

public class DateConversionException extends RuntimeException {

    public DateConversionException(Throwable cause) {
        super("Error converting string to date: " + cause);
    }
}
