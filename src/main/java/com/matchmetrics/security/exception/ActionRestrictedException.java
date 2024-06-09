/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package com.matchmetrics.security.exception;

public class ActionRestrictedException extends RuntimeException {
    public ActionRestrictedException(String email) {
        super("Action restricted for " + email);
    }
}
