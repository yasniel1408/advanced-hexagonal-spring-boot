package com.yascode.domain.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidStatusException extends RuntimeException implements DomainException{
    public InvalidStatusException(String currentStatus) {
        super("Invalid status value: " + currentStatus);
    }
}
