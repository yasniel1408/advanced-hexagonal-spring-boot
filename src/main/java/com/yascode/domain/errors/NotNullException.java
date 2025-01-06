package com.yascode.domain.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotNullException extends RuntimeException implements DomainException{
    public NotNullException(String field) {
        super(field + " cannot be null or empty.");
    }
}
