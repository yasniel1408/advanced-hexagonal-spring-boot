package com.yascode.application.usecases;

import com.yascode.domain.errors.NotFoundCustomerException;
import com.yascode.domain.ports.CustomerDbRepositoryPort;
import com.yascode.infrastructure.in.http.response.CustomerResponseDto;
import com.yascode.infrastructure.out.jpa_db.CustomerDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetCustomerByIdUseCaseTest {

    @Mock
    private CustomerDbRepositoryPort<CustomerDao> customerDbAdapter;

    @InjectMocks
    private GetCustomerByIdUseCase getCustomerByIdUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_returnsCustomerResponseDtoWhenCustomerExists() {
        // ID del cliente que existe
        Integer customerId = 1;

        // Simular un cliente encontrado
        CustomerDao customerDao = new CustomerDao();
        customerDao.setId(customerId);
        customerDao.setName("John Doe");
        customerDao.setAge(30);
        customerDao.setEmail("john.doe@example.com");
        customerDao.setStatus("ACTIVE");

        when(customerDbAdapter.findById(customerId)).thenReturn(Optional.of(customerDao));

        // Ejecutar el caso de uso
        CustomerResponseDto response = getCustomerByIdUseCase.execute(customerId);

        // Verificar resultados
        assertNotNull(response);
        assertEquals(customerId, response.id());
        assertEquals("John Doe", response.name());
        assertEquals(30, response.age());
        assertEquals("john.doe@example.com", response.email());
        assertEquals("ACTIVE", response.status());

        // Verificar interacción con el mock
        verify(customerDbAdapter, times(1)).findById(customerId);
    }

    @Test
    void execute_throwsNotFoundCustomerExceptionWhenCustomerDoesNotExist() {
        // ID del cliente que no existe
        Integer customerId = 999;

        // Simular que no se encuentra el cliente
        when(customerDbAdapter.findById(customerId)).thenReturn(Optional.empty());

        // Ejecutar y verificar que lanza la excepción
        NotFoundCustomerException exception = assertThrows(
                NotFoundCustomerException.class,
                () -> getCustomerByIdUseCase.execute(customerId),
                "Se esperaba que lanzara NotFoundCustomerException"
        );

        assertEquals("Not found customer width id: 999", exception.getMessage());

        // Verificar interacción con el mock
        verify(customerDbAdapter, times(1)).findById(customerId);
    }
}