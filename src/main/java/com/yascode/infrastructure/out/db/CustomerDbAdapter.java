package com.yascode.infrastructure.out.db;

import com.yascode.domain.ports.CustomerDbRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerDbAdapter implements CustomerDbRepositoryPort<CustomerDao> {

    private final JpaCustomerRepository repository;

    public CustomerDbAdapter(JpaCustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<CustomerDao> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public boolean existByEmail(String email) {
        return repository.existsCustomerDaoByEmail(email);
    }

    @Override
    public CustomerDao save(CustomerDao customerDao) {
        return repository.save(customerDao);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<CustomerDao> getAll() {
        return repository.findAll();
    }

    @Override
    public CustomerDao update(CustomerDao customerDao) {
       return repository.save(customerDao);
    }
}