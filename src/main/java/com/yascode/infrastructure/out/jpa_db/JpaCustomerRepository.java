package com.yascode.infrastructure.out.jpa_db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCustomerRepository extends JpaRepository<CustomerDao, Integer> {

    public boolean existsCustomerDaoByEmail(String email);
    public boolean existsCustomerDaoById(Integer id);
}