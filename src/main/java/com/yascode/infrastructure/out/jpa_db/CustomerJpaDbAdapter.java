package com.yascode.infrastructure.out.jpa_db;

import com.yascode.domain.ports.CustomerDbRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("jpa")
@Transactional
public class CustomerJpaDbAdapter implements CustomerDbRepositoryPort<CustomerDao> {

    private final JpaCustomerRepository repository;

    public CustomerJpaDbAdapter(JpaCustomerRepository repository) {
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
    public boolean existById(Integer id) {
        return repository.existsCustomerDaoById(id);
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