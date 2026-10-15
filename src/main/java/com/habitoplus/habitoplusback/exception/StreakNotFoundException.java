package com.habitoplus.habitoplusback.exception;

public class StreakNotFoundException extends RuntimeException{
    public StreakNotFoundException(String message) {
        super(message);
    }

    public StreakNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
