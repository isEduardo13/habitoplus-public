package com.habitoplus.habitoplusback.exception;

public class PixelaConnectionException extends RuntimeException{
    public PixelaConnectionException(String message){
        super(message);
    }

    public PixelaConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
