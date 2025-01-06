package com.yascode.domain.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomerEmailUniqueException extends RuntimeException implements DomainException {
    public CustomerEmailUniqueException(String email) {
        super("Email " + email + " already exist in other customer");
    }
}
