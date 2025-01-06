package com.yascode.application.usecases;

import com.yascode.application.usecases.base.BaseUseCase;
import com.yascode.application.usecases.base.BaseUseCaseWithoutInput;
import com.yascode.infrastructure.in.http.response.CustomerResponseDto;
import com.yascode.infrastructure.out.db.CustomerDao;
import com.yascode.infrastructure.out.db.CustomerDbAdapter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetCustomersUseCase extends BaseUseCaseWithoutInput<List<CustomerResponseDto>> {

    private final CustomerDbAdapter customerDbAdapter;

    public GetCustomersUseCase(CustomerDbAdapter customerDbAdapter) {
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
