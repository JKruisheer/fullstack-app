package com.assignment.client_management.controllers;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class ClientCookieHandler {
    private static final String COOKIE_NAME = "RABO_CLIENTS";
    private static final int DURATION_IN_DAYS = 1;

    public String buildResponseCookieString(int clientCount) {
        return ResponseCookie.from(COOKIE_NAME, String.valueOf(clientCount))
                .httpOnly(false)
                .secure(false)
                .maxAge(Duration.ofDays(DURATION_IN_DAYS))
                .path("/")
                .build()
                .toString();
    }
}
