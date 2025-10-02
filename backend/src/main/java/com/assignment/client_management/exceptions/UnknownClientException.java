package com.assignment.client_management.exceptions;

public class UnknownClientException extends RuntimeException {
    public UnknownClientException(final String message) {
        super(message);
    }
}
