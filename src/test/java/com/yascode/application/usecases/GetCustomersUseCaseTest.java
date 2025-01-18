package com.yascode.application.usecases;

import com.yascode.domain.ports.CustomerDbRepositoryPort;
import com.yascode.infrastructure.in.http.response.CustomerResponseDto;
import com.yascode.infrastructure.out.jdbc_db.CustomerDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetCustomersUseCaseTest {

    @Mock
    private CustomerDbRepositoryPort<CustomerDao> customerDbAdapter;

    @InjectMocks
    private GetCustomersUseCase getCustomersUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_returnsListOfCustomerResponseDto() {
        // Simular una lista de CustomerDao
        CustomerDao customer1 = new CustomerDao();
        customer1.setId(1);
        customer1.setName("John Doe");
        customer1.setAge(30);
        customer1.setEmail("john.doe@example.com");
        customer1.setStatus("ACTIVE");

        CustomerDao customer2 = new CustomerDao();
        customer2.setId(2);
        customer2.setName("Jane Doe");
        customer2.setAge(25);
        customer2.setEmail("jane.doe@example.com");
        customer2.setStatus("INACTIVE");

        List<CustomerDao> mockCustomers = Arrays.asList(customer1, customer2);

        // Configurar el mock para que devuelva la lista simulada
        when(customerDbAdapter.getAll()).thenReturn(mockCustomers);

        // Ejecutar el caso de uso
        List<CustomerResponseDto> response = getCustomersUseCase.execute();

        // Verificar resultados
        assertNotNull(response);
        assertEquals(2, response.size());

        // Verificar los datos del primer cliente
        CustomerResponseDto responseCustomer1 = response.get(0);
        assertEquals(1, responseCustomer1.id());
        assertEquals("John Doe", responseCustomer1.name());
        assertEquals(30, responseCustomer1.age());
        assertEquals("john.doe@example.com", responseCustomer1.email());
        assertEquals("ACTIVE", responseCustomer1.status());

        // Verificar los datos del segundo cliente
        CustomerResponseDto responseCustomer2 = response.get(1);
        assertEquals(2, responseCustomer2.id());
        assertEquals("Jane Doe", responseCustomer2.name());
        assertEquals(25, responseCustomer2.age());
        assertEquals("jane.doe@example.com", responseCustomer2.email());
        assertEquals("INACTIVE", responseCustomer2.status());

        // Verificar que se llamó al método getAll del mock
        verify(customerDbAdapter, times(1)).getAll();
    }

    @Test
    void execute_returnsEmptyListWhenNoCustomersExist() {
        // Configurar el mock para que devuelva una lista vacía
        when(customerDbAdapter.getAll()).thenReturn(List.of());

        // Ejecutar el caso de uso
        List<CustomerResponseDto> response = getCustomersUseCase.execute();

        // Verificar que la lista retornada esté vacía
        assertNotNull(response);
        assertTrue(response.isEmpty());

        // Verificar que se llamó al método getAll del mock
        verify(customerDbAdapter, times(1)).getAll();
    }
}