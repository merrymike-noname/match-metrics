/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package com.matchmetrics.security.exception;

public class UserDoesNotExistException extends RuntimeException{
    public UserDoesNotExistException(String email) {
        super("User with email '" + email + "' does not exist");
    }
}
