package com.yascode.domain.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundCustomerException extends RuntimeException implements DomainException {
    public NotFoundCustomerException(Integer id) {
        super("Not found customer width id: " + id);
    }
}
