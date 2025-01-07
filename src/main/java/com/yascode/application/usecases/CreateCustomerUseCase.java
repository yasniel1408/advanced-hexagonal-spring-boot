package com.yascode.application.usecases;

import com.yascode.application.usecases.base.BaseUseCase;
import com.yascode.domain.CustomerFactory;
import com.yascode.domain.entities.Customer;
import com.yascode.domain.errors.CustomerEmailUniqueException;
import com.yascode.domain.ports.CustomerDbRepositoryPort;
import com.yascode.infrastructure.in.http.request.CreateCustomerRequestDto;
import com.yascode.infrastructure.in.http.response.CustomerResponseDto;
import com.yascode.infrastructure.out.jpa_db.CustomerDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CreateCustomerUseCase extends BaseUseCase<CreateCustomerRequestDto, CustomerResponseDto> {

    private final CustomerDbRepositoryPort<CustomerDao> customerDbAdapter;

    public CreateCustomerUseCase(@Qualifier("jpa") CustomerDbRepositoryPort<CustomerDao> customerDbAdapter) {
        this.customerDbAdapter = customerDbAdapter;
    }


    public CustomerResponseDto execute(CreateCustomerRequestDto requestDto) {
        Customer customer = CustomerFactory.createCustomer(
                null,
                requestDto.name(),
                requestDto.email(),
                requestDto.age(),
                requestDto.status()
        );

        boolean existEmail = customerDbAdapter.existByEmail(requestDto.email());

        if (existEmail) {
            throw new CustomerEmailUniqueException(requestDto.email());
        }

        CustomerDao customerDao = new CustomerDao();
        customerDao.setName(customer.getName());
        customerDao.setEmail(customer.getEmail().getEmail());
        customerDao.setStatus(customer.getStatus().toString());
        customerDao.setAge(customer.getAge());
        CustomerDao customerDaoCreated = customerDbAdapter.save(customerDao);

        return mapToCustomerResponseDto(customerDaoCreated);
    }

    private CustomerResponseDto mapToCustomerResponseDto(CustomerDao customer) {
        return new CustomerResponseDto(customer.getId(), customer.getName(), customer.getAge(), customer.getEmail(), customer.getStatus());
    }
}
