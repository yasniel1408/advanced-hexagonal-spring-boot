package com.yascode.infrastructure.in.http.response;

public record CustomerResponseDto(Integer id, String name, Integer age, String email, String status) {
}
