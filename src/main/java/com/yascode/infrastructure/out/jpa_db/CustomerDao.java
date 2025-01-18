package com.yascode.infrastructure.out.jpa_db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity(name="customer")
@Getter
@Setter
// otra manera de hacer Constraints unique
//@Table(
//        name = "customer",
//        uniqueConstraints =
//                {
//                        @UniqueConstraint(
//                                name = "customer_email_unique",
//                                columnNames = "email"
//                        )
//                }
//)
public class CustomerDao {
    @Id
    @SequenceGenerator(
            name = "customer_id_seq",
            sequenceName = "customer_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_seq"
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
            nullable = false,
            unique = true // Constraints unique
    )
    private String email;

}
