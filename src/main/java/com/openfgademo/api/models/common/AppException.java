package com.openfgademo.api.models.common;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final String message;
    private final HttpStatus httpStatus;
    private final String internalCode;

    public AppException(String message, HttpStatus httpStatus, String internalCode) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
        this.internalCode = internalCode;
    }

    public AppException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
        this.internalCode = null;
    }
}
