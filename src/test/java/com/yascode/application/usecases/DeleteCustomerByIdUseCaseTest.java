package com.yascode.application.usecases;

import com.yascode.domain.ports.CustomerDbRepositoryPort;
import com.yascode.infrastructure.out.jpa_db.CustomerDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class DeleteCustomerByIdUseCaseTest {

    @Mock
    private CustomerDbRepositoryPort<CustomerDao> customerDbAdapter;

    @InjectMocks
    private DeleteCustomerByIdUseCase deleteCustomerByIdUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_deletesCustomerSuccessfully() {
        // ID del cliente a eliminar
        Integer customerId = 1;

        // Ejecutar el caso de uso
        deleteCustomerByIdUseCase.execute(customerId);

        // Verificar que se llamó al método deleteById con el ID correcto
        verify(customerDbAdapter, times(1)).deleteById(customerId);
    }
}