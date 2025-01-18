package com.yascode.application.usecases;

import com.yascode.domain.errors.CustomerEmailUniqueException;
import com.yascode.domain.errors.NotFoundCustomerException;
import com.yascode.domain.errors.NotNullException;
import com.yascode.domain.ports.CustomerDbRepositoryPort;
import com.yascode.infrastructure.in.http.request.UpdateCustomerRequestDto;
import com.yascode.infrastructure.in.http.response.CustomerResponseDto;
import com.yascode.infrastructure.out.jpa_db.CustomerDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateCustomerUseCaseTest {

    @Mock
    private CustomerDbRepositoryPort<CustomerDao> customerDbAdapter;

    @InjectMocks
    private UpdateCustomerUseCase updateCustomerUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_updatesCustomerSuccessfully() {
        // Datos de entrada
        UpdateCustomerRequestDto requestDto = new UpdateCustomerRequestDto(1, "John Doe", 30,"john.doe@example.com",  "ACTIVE");

        // Simular existencia del ID y no existencia del email
        when(customerDbAdapter.existById(requestDto.id())).thenReturn(true);
        when(customerDbAdapter.existByEmail(requestDto.email())).thenReturn(false);

        // Configurar el mock para la actualización
        CustomerDao updatedCustomerDao = new CustomerDao();
        updatedCustomerDao.setId(1);
        updatedCustomerDao.setName("John Doe");
        updatedCustomerDao.setEmail("john.doe@example.com");
        updatedCustomerDao.setAge(30);
        updatedCustomerDao.setStatus("ACTIVE");

        when(customerDbAdapter.update(any(CustomerDao.class))).thenReturn(updatedCustomerDao);

        // Ejecutar el caso de uso
        CustomerResponseDto response = updateCustomerUseCase.execute(requestDto);

        // Verificar resultados
        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals("John Doe", response.name());
        assertEquals("john.doe@example.com", response.email());
        assertEquals(30, response.age());
        assertEquals("ACTIVE", response.status());

        // Verificar interacciones con el mock
        verify(customerDbAdapter).existById(requestDto.id());
        verify(customerDbAdapter).existByEmail(requestDto.email());
        verify(customerDbAdapter).update(any(CustomerDao.class));
    }

    @Test
    void execute_throwsNotNullExceptionWhenIdIsNull() {
        // Datos de entrada con ID nulo
        UpdateCustomerRequestDto requestDto = new UpdateCustomerRequestDto(null, "Jane Doe", 25,"jane.doe@example.com",  "ACTIVE");

        // Ejecutar y verificar que lanza la excepción
        NotNullException exception = assertThrows(
                NotNullException.class,
                () -> updateCustomerUseCase.execute(requestDto)
        );

        assertEquals("Id cannot be null or empty.", exception.getMessage());

        // Verificar que no se llama a otros métodos
        verify(customerDbAdapter, never()).existById(anyInt());
        verify(customerDbAdapter, never()).existByEmail(anyString());
        verify(customerDbAdapter, never()).update(any(CustomerDao.class));
    }

    @Test
    void execute_throwsNotFoundCustomerExceptionWhenIdDoesNotExist() {
        // Datos de entrada con ID no existente
        UpdateCustomerRequestDto requestDto = new UpdateCustomerRequestDto(999, "Jane Doe", 25, "jane.doe@example.com", "ACTIVE");

        // Simular que el ID no existe
        when(customerDbAdapter.existById(requestDto.id())).thenReturn(false);

        // Ejecutar y verificar que lanza la excepción
        NotFoundCustomerException exception = assertThrows(
                NotFoundCustomerException.class,
                () -> updateCustomerUseCase.execute(requestDto)
        );

        assertEquals("Not found customer width id: 999", exception.getMessage());

        // Verificar interacciones
        verify(customerDbAdapter).existById(requestDto.id());
        verify(customerDbAdapter, never()).existByEmail(anyString());
        verify(customerDbAdapter, never()).update(any(CustomerDao.class));
    }

    @Test
    void execute_throwsCustomerEmailUniqueExceptionWhenEmailExists() {
        // Datos de entrada con email existente
        UpdateCustomerRequestDto requestDto = new UpdateCustomerRequestDto(1, "Jane Doe", 25,"jane.doe@example.com",  "ACTIVE");

        // Simular existencia del ID y del email
        when(customerDbAdapter.existById(requestDto.id())).thenReturn(true);
        when(customerDbAdapter.existByEmail(requestDto.email())).thenReturn(true);

        // Ejecutar y verificar que lanza la excepción
        CustomerEmailUniqueException exception = assertThrows(
                CustomerEmailUniqueException.class,
                () -> updateCustomerUseCase.execute(requestDto)
        );

        assertEquals("Email jane.doe@example.com already exist in other customer", exception.getMessage());

        // Verificar interacciones
        verify(customerDbAdapter).existById(requestDto.id());
        verify(customerDbAdapter).existByEmail(requestDto.email());
        verify(customerDbAdapter, never()).update(any(CustomerDao.class));
    }
}