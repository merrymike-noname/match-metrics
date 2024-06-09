/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package com.matchmetrics.security.exception;

public class EmailTakenException extends RuntimeException {
    public EmailTakenException(String email) {
        super("Email '" + email + "' is already taken");
    }
}
