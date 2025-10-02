package com.assignment.client_management.controllers;

import com.assignment.client_management.exceptions.UnknownClientException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ClientExceptionHandler {

    @ExceptionHandler(UnknownClientException.class)
    public ResponseEntity<String> handleUnknownClientException(final UnknownClientException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}
