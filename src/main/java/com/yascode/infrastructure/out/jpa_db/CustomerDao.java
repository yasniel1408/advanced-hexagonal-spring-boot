package com.yascode.infrastructure.out.jpa_db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name="customer")
@Getter
@Setter
public class CustomerDao {
    @Id
    @SequenceGenerator(
            name = "customer_id_sequence",
            sequenceName = "customer_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_sequence"
    )
    private  Integer id;

    @Column(
            nullable = false
    )
    private String name;

    @Column(
            nullable = false
    )
    private Integer age;

    @Column(
            nullable = false
    )
    private String status;

    @Column(
            nullable = false
    )
    private String email;
}
