package com.yascode.domain.entities;

import com.yascode.domain.errors.NotNullException;
import com.yascode.domain.value_objects.Email;
import com.yascode.domain.value_objects.Status;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Customer {
    private final Integer id;
    private final String name;
    private final Integer age;
    private final Status status;
    private final Email email;

    private Customer(Builder builder) {
        if (builder.name == null || builder.name.isEmpty()) {
            throw new NotNullException("Name");
        }
        if (builder.age < 0) {
            throw new NotNullException("Age");
        }

        this.id = builder.id;
        this.name = builder.name;
        this.age = builder.age;
        this.status = builder.status;
        this.email = builder.email;
    }

    public static class Builder {
        private Integer id;
        private String name;
        private Integer age;
        private Status status;
        private Email email;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAge(Integer age) {
            this.age = age;
            return this;
        }

        public Builder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder setEmail(Email email) {
            this.email = email;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}