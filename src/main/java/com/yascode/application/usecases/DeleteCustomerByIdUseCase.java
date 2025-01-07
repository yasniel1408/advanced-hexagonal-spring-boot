package com.yascode.application.usecases;

import com.yascode.application.usecases.base.BaseUseCaseWithoutOutput;
import com.yascode.domain.ports.CustomerDbRepositoryPort;
import com.yascode.infrastructure.out.jpa_db.CustomerDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DeleteCustomerByIdUseCase extends BaseUseCaseWithoutOutput<Integer> {

    private final CustomerDbRepositoryPort<CustomerDao> customerDbAdapter;

    public DeleteCustomerByIdUseCase(@Qualifier("jpa") CustomerDbRepositoryPort<CustomerDao> customerDbAdapter) {
        this.customerDbAdapter = customerDbAdapter;
    }

    public void execute(Integer customerId) {
        customerDbAdapter.deleteById(customerId);
    }
}
