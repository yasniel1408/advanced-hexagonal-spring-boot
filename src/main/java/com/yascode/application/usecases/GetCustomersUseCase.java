package com.yascode.application.usecases;

import com.yascode.application.usecases.base.BaseUseCaseWithoutInput;
import com.yascode.domain.ports.CustomerDbRepositoryPort;
import com.yascode.infrastructure.in.http.response.CustomerResponseDto;
import com.yascode.infrastructure.out.jdbc_db.CustomerDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetCustomersUseCase extends BaseUseCaseWithoutInput<List<CustomerResponseDto>> {

    private final CustomerDbRepositoryPort<CustomerDao> customerDbAdapter;

    public GetCustomersUseCase(@Qualifier("jdbc") CustomerDbRepositoryPort<CustomerDao> customerDbAdapter) {
        this.customerDbAdapter = customerDbAdapter;
    }

    public List<CustomerResponseDto> execute() {
        List<CustomerDao> customerDaos = customerDbAdapter.getAll();
        return mapToCustomerResponseDto(customerDaos);
    }

    private List<CustomerResponseDto> mapToCustomerResponseDto(List<CustomerDao> list) {
        List<CustomerResponseDto> customerResponseDtos = new ArrayList<>();
        list.forEach(customerDao -> {
            customerResponseDtos.add(new CustomerResponseDto(customerDao.getId(), customerDao.getName(), customerDao.getAge(), customerDao.getEmail(), customerDao.getStatus()));
        });
        return customerResponseDtos;
    }
}
