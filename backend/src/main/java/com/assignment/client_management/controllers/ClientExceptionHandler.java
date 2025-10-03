package com.assignment.client_management.controllers;

import com.assignment.client_management.controllers.problems.GenericProblem;
import com.assignment.client_management.controllers.problems.Problem;
import com.assignment.client_management.controllers.problems.UnknownClientProblem;
import com.assignment.client_management.exceptions.UnknownClientException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ClientExceptionHandler {

    @ExceptionHandler(UnknownClientException.class)
    public ResponseEntity<Problem> handleUnknownClientException(final UnknownClientException ex) {
        UnknownClientProblem problem = new UnknownClientProblem(404, ex.getMessage());
        return ResponseEntity.status(problem.getStatus()).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Problem> handleGenericError(final Exception ex) {
        Problem problem = new GenericProblem();
        return ResponseEntity.status(problem.getStatus()).body(problem);
    }
}
