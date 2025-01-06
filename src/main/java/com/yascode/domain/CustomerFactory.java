package com.yascode.domain;

import com.yascode.domain.entities.Customer;
import com.yascode.domain.errors.InvalidStatusException;
import com.yascode.domain.errors.NotNullException;
import com.yascode.domain.value_objects.Email;
import com.yascode.domain.value_objects.Status;

public class CustomerFactory {

    public static Customer createCustomer(Integer id, String name, String email, int age, String status) {
        if (email == null || email.isEmpty()) {
            throw new NotNullException("Email");
        }
        if (status == null || status.isEmpty()) {
            throw new NotNullException("Status");
        }

        // Convert the String status to an enum value
        Status statusVo;
        try {
            statusVo = Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidStatusException(status);
        }

        Email emailVo = new Email(email);

        return new Customer.Builder()
                .setId(id)
                .setName(name)
                .setAge(age)
                .setStatus(statusVo)
                .setEmail(emailVo)
                .build();
    }
}