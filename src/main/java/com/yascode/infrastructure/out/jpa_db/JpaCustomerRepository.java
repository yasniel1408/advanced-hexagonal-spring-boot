package com.yascode.infrastructure.out.jpa_db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaCustomerRepository extends JpaRepository<CustomerDao, Integer> {

    public boolean existsCustomerDaoByEmail(String email);
    public boolean existsCustomerDaoById(Integer id);

    @Query(value = "SELECT * FROM customer_dao WHERE email = :email", nativeQuery = true)
    Optional<CustomerDao> findCustomerByEmailNative(@Param("email") String email);

    @Query(value = "SELECT * FROM customer_dao WHERE name LIKE %:name%", nativeQuery = true)
    List<CustomerDao> searchByNameContaining(@Param("name") String name);
}