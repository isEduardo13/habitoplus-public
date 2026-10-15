package com.habitoplus.habitoplusback.exception;

public class GraphPixelaException extends RuntimeException {

    private final int statusCode;

    public GraphPixelaException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
