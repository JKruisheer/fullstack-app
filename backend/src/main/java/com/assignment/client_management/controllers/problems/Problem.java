package com.assignment.client_management.controllers.problems;

public abstract class Problem {
    private final int status;
    private final String translation;

    public Problem(int status, String translation) {
        this.status = status;
        this.translation = translation;
    }

    public int getStatus() {
        return status;
    }

    public String getTranslation() {
        return translation;
    }
}
