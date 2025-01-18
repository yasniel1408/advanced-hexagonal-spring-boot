package com.yascode.infrastructure.out.jdbc_db;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDao {
    private Integer id;
    private String name;
    private Integer age;
    private String status;
    private String email;
}
