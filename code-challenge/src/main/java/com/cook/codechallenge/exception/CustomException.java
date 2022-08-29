package com.cook.codechallenge.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus statusCode;

    public CustomException(final String message, final HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
