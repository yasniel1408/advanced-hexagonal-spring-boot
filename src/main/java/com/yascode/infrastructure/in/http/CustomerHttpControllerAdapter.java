package com.yascode.infrastructure.in.http;

import com.yascode.application.usecases.*;
import com.yascode.domain.ports.CustomerHttpRepositoryPort;
import com.yascode.infrastructure.in.http.request.CreateCustomerRequestDto;
import com.yascode.infrastructure.in.http.request.UpdateCustomerRequestDto;
import com.yascode.infrastructure.in.http.response.CustomerResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerHttpControllerAdapter implements CustomerHttpRepositoryPort<CustomerResponseDto, CreateCustomerRequestDto, UpdateCustomerRequestDto> {
    private final GetCustomersUseCase getCustomersUseCase;
    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;
    private final DeleteCustomerByIdUseCase deleteCustomerById;
    private final UpdateCustomerUseCase updateCustomerUseCase;

    public CustomerHttpControllerAdapter(GetCustomersUseCase getCustomersUseCase, CreateCustomerUseCase createCustomerUseCase, GetCustomerByIdUseCase getCustomerByIdUseCase, DeleteCustomerByIdUseCase deleteCustomerById, UpdateCustomerUseCase updateCustomerUseCase) {
        this.getCustomersUseCase = getCustomersUseCase;
        this.createCustomerUseCase = createCustomerUseCase;
        this.getCustomerByIdUseCase = getCustomerByIdUseCase;
        this.deleteCustomerById = deleteCustomerById;
        this.updateCustomerUseCase = updateCustomerUseCase;
    }

    @GetMapping
    public List<CustomerResponseDto> getCustomers () {
        return getCustomersUseCase.run();
    }

    @PostMapping
    public CustomerResponseDto createCustomer (@RequestBody CreateCustomerRequestDto customer) {
        return createCustomerUseCase.run(customer);
    }

    @GetMapping("{customerId}")
    public CustomerResponseDto getById(@PathVariable("customerId") Integer customerId) {
        return getCustomerByIdUseCase.run(customerId);
    }

    @DeleteMapping("{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("customerId") Integer customerId) {
        deleteCustomerById.run(customerId);
    }

    @PutMapping
    public CustomerResponseDto updateCustomer(@RequestBody UpdateCustomerRequestDto customer) {
        return updateCustomerUseCase.run(customer);
    }

}
