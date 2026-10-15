package com.habitoplus.habitoplusback.exception;

public class UserAlreadyExistsException extends RuntimeException {
        
        public UserAlreadyExistsException(String message) {
            super(message);
        }

}
