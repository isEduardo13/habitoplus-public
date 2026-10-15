package com.habitoplus.habitoplusback.exception;

public class GraphPostException extends RuntimeException{
    public GraphPostException(String message, Throwable cause) {
        super(message, cause);
    }

    public GraphPostException(String message) {
        super(message);
    }
}
