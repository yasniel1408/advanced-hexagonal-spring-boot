package com.yascode.infrastructure.in.http.request;

public record CreateCustomerRequestDto(String name, Integer age, String email, String status) {
}
