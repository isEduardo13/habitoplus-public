package com.habitoplus.habitoplusback.exception;


public class PhotoUploadException  extends RuntimeException{
    public PhotoUploadException(String mensaje) {
        super(mensaje);
    }

    public PhotoUploadException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
