package com.habitoplus.habitoplusback.exception;

public class GroupEmptyException extends RuntimeException{
    public GroupEmptyException(String mensaje){
        super(mensaje);
    }
}
