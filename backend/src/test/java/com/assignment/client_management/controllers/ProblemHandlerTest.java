package com.assignment.client_management.controllers;

import com.assignment.client_management.controllers.problems.Problem;
import com.assignment.client_management.exceptions.DataInputException;
import com.assignment.client_management.exceptions.DataValidationException;
import com.assignment.client_management.exceptions.UnknownClientException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProblemHandlerTest {
    private static final String EXPECTED_MESSAGE = "Expected message";

    @InjectMocks
    private ProblemHandler problemHandler;

    @Test
    void testHandleUnknownClientException() {
        UnknownClientException ex = new UnknownClientException(EXPECTED_MESSAGE);
        ResponseEntity<Problem> response = problemHandler.handleUnknownClientException(ex);

        assertEquals(404, response.getStatusCode().value());
        verifyProblem(response.getBody(), 404);
    }


    @ParameterizedTest
    @MethodSource("dataProblemExceptions")
    void testHandleDataProblems(Exception ex) {
        ResponseEntity<Problem> response = problemHandler.handleDataProblems(ex);

        assertEquals(400, response.getStatusCode().value());
        verifyProblem(response.getBody(), 400);
    }

    private static Stream<Exception> dataProblemExceptions() {
        return Stream.of(
                new DataValidationException(EXPECTED_MESSAGE),
                new DataInputException(EXPECTED_MESSAGE)
        );
    }

    @Test
    void testHandleGenericException() {
        Exception ex = new Exception(EXPECTED_MESSAGE);
        ResponseEntity<Problem> response = problemHandler.handleGenericException(ex);

        assertEquals(500, response.getStatusCode().value());
        verifyProblem(response.getBody(), 500);
    }

    private void verifyProblem(Problem problem, int expectedStatus) {
        assertEquals(EXPECTED_MESSAGE, problem.getTranslation());
        assertEquals(expectedStatus, problem.getStatus());
    }

}
