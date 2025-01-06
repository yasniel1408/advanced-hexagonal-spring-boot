package com.yascode.domain.ports;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerHttpRepositoryPort<RS, RQ, UP> {
    public List<RS> getCustomers();
    public RS createCustomer(RQ customer);
    public RS getById(Integer id);
    public void deleteById(Integer id);
    public RS updateCustomer(UP customer);
}
