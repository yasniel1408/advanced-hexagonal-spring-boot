package com.yascode.infrastructure.out.jdbc_db;

import com.yascode.domain.ports.CustomerDbRepositoryPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("jdbc")
public class CustomerJDBCAdapter implements CustomerDbRepositoryPort<CustomerDao> {

    private final JdbcTemplate jdbcTemplate;

    public CustomerJDBCAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CustomerDao> customerRowMapper = (rs, rowNum) -> {
        CustomerDao customer = new CustomerDao();
        customer.setId(rs.getInt("id"));
        customer.setName(rs.getString("name"));
        customer.setAge(rs.getInt("age"));
        customer.setStatus(rs.getString("status"));
        customer.setEmail(rs.getString("email"));
        return customer;
    };

    @Override
    public Optional<CustomerDao> findById(Integer id) {
        String sql = "SELECT * FROM customer WHERE id = ?";
        List<CustomerDao> customers = jdbcTemplate.query(sql, customerRowMapper, id);
        return customers.isEmpty() ? Optional.empty() : Optional.of(customers.get(0));
    }

    @Override
    public boolean existByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM customer WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existById(Integer id) {
        String sql = "SELECT COUNT(*) FROM customer WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public CustomerDao save(CustomerDao customer) {
        String sql = "INSERT INTO customer (name, age, status, email) VALUES (?, ?, ?, ?) RETURNING id";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class,
                customer.getName(), customer.getAge(), customer.getStatus(), customer.getEmail());
        customer.setId(id);
        return customer;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM customer WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<CustomerDao> getAll() {
        String sql = "SELECT * FROM customer";
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public CustomerDao update(CustomerDao customer) {
        String sql = "UPDATE customer SET name = ?, age = ?, status = ?, email = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                customer.getName(),
                customer.getAge(),
                customer.getStatus(),
                customer.getEmail(),
                customer.getId());
        return customer;
    }
}
