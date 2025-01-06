package com.yascode.domain.entities;

import com.yascode.domain.errors.NotNullException;
import com.yascode.domain.value_objects.Email;
import com.yascode.domain.value_objects.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {
    private Integer id;
    private final String name;
    private final Integer age;
    private Status status;
    private Email email;

    public Customer(String name, Integer age) {
        if (name == null || name.isEmpty()) {
            throw new NotNullException("Name");
        }
        if (age < 0) {
            throw new NotNullException("Age");
        }

        this.name = name;
        this.age = age;
    }
}