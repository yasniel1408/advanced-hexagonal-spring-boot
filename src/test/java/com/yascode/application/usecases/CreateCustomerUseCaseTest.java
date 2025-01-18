package com.yascode.application.usecases;

import com.yascode.domain.errors.CustomerEmailUniqueException;
import com.yascode.domain.ports.CustomerDbRepositoryPort;
import com.yascode.infrastructure.in.http.request.CreateCustomerRequestDto;
import com.yascode.infrastructure.in.http.response.CustomerResponseDto;
import com.yascode.infrastructure.out.jpa_db.CustomerDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateCustomerUseCaseTest {

    @Mock
    private CustomerDbRepositoryPort<CustomerDao> customerDbAdapter;

    @InjectMocks
    private CreateCustomerUseCase createCustomerUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_createsCustomerSuccessfully() {
        // Datos de entrada
        CreateCustomerRequestDto requestDto = new CreateCustomerRequestDto("John Doe", 30, "john.doe@example.com", "ACTIVE");

        // Configurar el mock para simular que el email no existe en la base de datos
        when(customerDbAdapter.existByEmail(requestDto.email())).thenReturn(false);

        // Configurar el mock para guardar un nuevo cliente
        CustomerDao savedCustomerDao = new CustomerDao();
        savedCustomerDao.setId(1);
        savedCustomerDao.setName("John Doe");
        savedCustomerDao.setEmail("john.doe@example.com");
        savedCustomerDao.setAge(30);
        savedCustomerDao.setStatus("ACTIVE");
        when(customerDbAdapter.save(any(CustomerDao.class))).thenReturn(savedCustomerDao);

        // Ejecutar el caso de uso
        CustomerResponseDto response = createCustomerUseCase.execute(requestDto);

        // Verificar el resultado
        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals("John Doe", response.name());
        assertEquals("john.doe@example.com", response.email());
        assertEquals(30, response.age());
        assertEquals("ACTIVE", response.status());

        // Verificar interacciones con el mock
        verify(customerDbAdapter).existByEmail(requestDto.email());
        verify(customerDbAdapter).save(any(CustomerDao.class));
    }

    @Test
    void execute_throwsExceptionIfEmailExists() {
        // Datos de entrada
        CreateCustomerRequestDto requestDto = new CreateCustomerRequestDto("Jane Doe", 25, "jane.doe@example.com", "ACTIVE");

        // Configurar el mock para simular que el email ya existe
        when(customerDbAdapter.existByEmail(requestDto.email())).thenReturn(true);

        // Ejecutar y verificar que lanza una excepción
        CustomerEmailUniqueException exception = assertThrows(
                CustomerEmailUniqueException.class,
                () -> createCustomerUseCase.execute(requestDto),
                "Se esperaba que lanzara una CustomerEmailUniqueException"
        );

        assertEquals("Email jane.doe@example.com already exist in other customer", exception.getMessage());

        // Verificar que no se haya intentado guardar el cliente
        verify(customerDbAdapter, never()).save(any(CustomerDao.class));
    }
}