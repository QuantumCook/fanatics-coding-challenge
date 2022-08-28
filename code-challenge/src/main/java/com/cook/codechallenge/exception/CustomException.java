package com.cook.codechallenge.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    final private HttpStatus statusCode;

    public CustomException (String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
