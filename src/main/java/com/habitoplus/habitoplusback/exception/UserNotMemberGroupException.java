package com.habitoplus.habitoplusback.exception;

public class UserNotMemberGroupException extends RuntimeException{
    public UserNotMemberGroupException(String message){
        super(message);
    }
}
