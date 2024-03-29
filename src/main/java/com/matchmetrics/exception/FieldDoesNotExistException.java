package com.matchmetrics.exception;

public class FieldDoesNotExistException extends IllegalArgumentException {
    public FieldDoesNotExistException(String field, Class<?> classWithoutField) {
        super("The field '" + field + "' does not exist in " + classWithoutField.getName());
    }
}