package com.yascode.domain.ports;

import java.util.List;
import java.util.Optional;

public interface CustomerDbRepositoryPort<C>{
    Optional<C> findById(Integer id);
    boolean existByEmail(String email);
    boolean existById(Integer id);
    C save(C customer);
    void deleteById(Integer id);
    List<C> getAll();
    C update(C customer);
}
