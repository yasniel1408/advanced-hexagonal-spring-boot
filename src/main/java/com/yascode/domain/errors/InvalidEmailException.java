package com.yascode.domain.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidEmailException extends RuntimeException implements DomainException{
    public InvalidEmailException(String email) {
        super("Invalid email format: " + email);
    }
}
