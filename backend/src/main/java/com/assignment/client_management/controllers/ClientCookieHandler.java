package com.assignment.client_management.controllers;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class ClientCookieHandler {

    public String buildResponseCookieString(int clientCount) {
        return ResponseCookie.from("RABO_CLIENTS", String.valueOf(clientCount))
                .httpOnly(false)
                .secure(false)
                .maxAge(Duration.ofDays(1))
                .path("/")
                .build()
                .toString();
    }
}
