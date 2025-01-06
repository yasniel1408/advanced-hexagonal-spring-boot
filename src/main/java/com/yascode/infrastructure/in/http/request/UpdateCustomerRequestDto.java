package com.yascode.infrastructure.in.http.request;

public record UpdateCustomerRequestDto(Integer id, String name, Integer age, String email, String status) {
}
