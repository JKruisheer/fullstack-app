package com.assignment.client_management.controllers;

import com.assignment.client_management.controllers.problems.DataProblem;
import com.assignment.client_management.controllers.problems.GenericProblem;
import com.assignment.client_management.controllers.problems.Problem;
import com.assignment.client_management.controllers.problems.UnknownClientProblem;
import com.assignment.client_management.exceptions.DataInputException;
import com.assignment.client_management.exceptions.DataValidationException;
import com.assignment.client_management.exceptions.UnknownClientException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProblemHandler {

    @ExceptionHandler(UnknownClientException.class)
    public ResponseEntity<Problem> handleUnknownClientException(final UnknownClientException ex) {
        UnknownClientProblem problem = new UnknownClientProblem(404, ex.getMessage());
        return ResponseEntity.status(problem.getStatus()).body(problem);
    }

    @ExceptionHandler({DataValidationException.class, DataInputException.class})
    public ResponseEntity<Problem> handleDataProblems(final Exception ex) {
        DataProblem problem = new DataProblem(400, ex.getMessage());
        return ResponseEntity.status(problem.getStatus()).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Problem> handleGenericException(final Exception ex) {
        Problem problem = new GenericProblem(ex.getMessage());
        return ResponseEntity.status(problem.getStatus()).body(problem);
    }
}
