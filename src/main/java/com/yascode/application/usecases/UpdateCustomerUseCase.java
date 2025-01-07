package com.yascode.application.usecases;

import com.yascode.application.usecases.base.BaseUseCase;
import com.yascode.domain.CustomerFactory;
import com.yascode.domain.entities.Customer;
import com.yascode.domain.errors.CustomerEmailUniqueException;
import com.yascode.domain.errors.NotFoundCustomerException;
import com.yascode.domain.errors.NotNullException;
import com.yascode.domain.ports.CustomerDbRepositoryPort;
import com.yascode.infrastructure.in.http.request.UpdateCustomerRequestDto;
import com.yascode.infrastructure.in.http.response.CustomerResponseDto;
import com.yascode.infrastructure.out.jpa_db.CustomerDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UpdateCustomerUseCase extends BaseUseCase<UpdateCustomerRequestDto, CustomerResponseDto> {

    private final CustomerDbRepositoryPort<CustomerDao> customerDbAdapter;

    public UpdateCustomerUseCase(@Qualifier("jpa") CustomerDbRepositoryPort<CustomerDao> customerDbAdapter) {
        this.customerDbAdapter = customerDbAdapter;
    }

    @Override
    public CustomerResponseDto execute(UpdateCustomerRequestDto requestDto) {

        if (requestDto.id() == null || requestDto.id() == 0) {
            throw new NotNullException("Id");
        }
        boolean existById = customerDbAdapter.existById(requestDto.id());
        if (!existById) {
            throw new NotFoundCustomerException(requestDto.id());
        }


        Customer customer = CustomerFactory.createCustomer(
                requestDto.id(),
                requestDto.name(),
                requestDto.email(),
                requestDto.age(),
                requestDto.status()
        );

        boolean existByEmail = customerDbAdapter.existByEmail(requestDto.email());

        if (existByEmail) {
            throw new CustomerEmailUniqueException(requestDto.email());
        }

        CustomerDao customerDao = new CustomerDao();
        customerDao.setId(customer.getId());
        customerDao.setName(customer.getName());
        customerDao.setEmail(customer.getEmail().getEmail());
        customerDao.setStatus(customer.getStatus().toString());
        customerDao.setAge(customer.getAge());

        CustomerDao customerDaoUpdated = customerDbAdapter.update(customerDao);

        return mapToCustomerResponseDto(customerDaoUpdated);
    }

    private CustomerResponseDto mapToCustomerResponseDto(CustomerDao customer) {
        return new CustomerResponseDto(customer.getId(), customer.getName(), customer.getAge(), customer.getEmail(), customer.getStatus());
    }
}
