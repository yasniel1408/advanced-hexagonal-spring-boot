package com.yascode.application.usecases;

import com.yascode.application.usecases.base.BaseUseCaseWithoutOutput;
import com.yascode.infrastructure.out.db.CustomerDbAdapter;
import org.springframework.stereotype.Service;

@Service
public class DeleteCustomerByIdUseCase extends BaseUseCaseWithoutOutput<Integer> {

    private final CustomerDbAdapter customerDbAdapter;

    public DeleteCustomerByIdUseCase(CustomerDbAdapter customerDbAdapter) {
        this.customerDbAdapter = customerDbAdapter;
    }

    public void execute(Integer customerId) {
        customerDbAdapter.deleteById(customerId);
    }
}
