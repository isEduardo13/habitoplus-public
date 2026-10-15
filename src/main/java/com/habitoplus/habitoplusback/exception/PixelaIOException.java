package com.habitoplus.habitoplusback.exception;

public class PixelaIOException extends RuntimeException{
    public PixelaIOException(String message) {
        super(message);
    }
    
    public PixelaIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
