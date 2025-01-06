package com.yascode.infrastructure.out.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCustomerRepository extends JpaRepository<CustomerDao, Integer> {

    public boolean existsCustomerDaoByEmail(String email);
}