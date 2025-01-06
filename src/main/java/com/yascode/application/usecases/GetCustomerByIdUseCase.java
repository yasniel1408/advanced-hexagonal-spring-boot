package com.yascode.application.usecases;

import com.yascode.application.usecases.base.BaseUseCase;
import com.yascode.domain.errors.NotFoundCustomerException;
import com.yascode.infrastructure.in.http.response.CustomerResponseDto;
import com.yascode.infrastructure.out.db.CustomerDao;
import com.yascode.infrastructure.out.db.CustomerDbAdapter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetCustomerByIdUseCase extends BaseUseCase<Integer, CustomerResponseDto> {
    private final CustomerDbAdapter customerDbAdapter;

    public GetCustomerByIdUseCase(CustomerDbAdapter customerDbAdapter) {
        this.customerDbAdapter = customerDbAdapter;
    }

    public CustomerResponseDto execute(Integer id) {
        Optional<CustomerDao> customerDao = customerDbAdapter.findById(id);

        if(customerDao.isEmpty()){
            throw new NotFoundCustomerException(id);
        }

        return mapToCustomerResponseDto(customerDao.get());
    }

    private CustomerResponseDto mapToCustomerResponseDto(CustomerDao customer) {
        return new CustomerResponseDto(customer.getId(), customer.getName(), customer.getAge(), customer.getEmail(), customer.getStatus());
    }
}
