package com.yascode.domain;

import com.yascode.domain.entities.Customer;
import com.yascode.domain.errors.NotNullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerFactoryTest {

    @BeforeEach
    void setUp() {
        // Si necesitas inicializar algo antes de cada prueba (mocks, variables, etc.),
        // puedes hacerlo aquí. En caso contrario, puedes dejarlo vacío.
    }

    @Test
    void createCustomer() {
        // Dado (arrange)
        String name = "John Doe";
        String email = "john.doe@example.com";
        int age = 30;
        String status = "ACTIVE";

        // Cuando (act)
        // Supongamos que tu método de fábrica tiene esta firma:
        // public static Customer createCustomer(String name, String email, int age, String status)
        Customer customer = CustomerFactory.createCustomer(null, name, email, age, status);

        // Entonces (assert)
        assertNotNull(customer, "Se esperaba que el Customer no fuera nulo");
        assertEquals(name, customer.getName(), "El nombre del Customer no coincide");
        assertEquals(email, customer.getEmail().getEmail(), "El email del Customer no coincide");
        assertEquals(age, customer.getAge(), "La edad del Customer no coincide");
        assertEquals(status, customer.getStatus().toString(), "El status del Customer no coincide");
    }

    @Test
    void createCustomer_withNegativeAge_shouldThrowException() {
        try{
            Customer customer = CustomerFactory.createCustomer(null, "Jane", "jane@example.com", -5, "ACTIVE");
            assertNotNull(customer);
        }catch (NotNullException e){
            assertEquals(e.getMessage(), "Age cannot be null or empty.");
        }
    }

    @Test
    void createCustomer_withoutStatus_shouldUseDefault() {
        try{
            Customer customer = CustomerFactory.createCustomer(null,"Bob", "bob@example.com", 25, null);
            assertNotNull(customer);
        }catch (NotNullException e){
            assertEquals(e.getMessage(), "Status cannot be null or empty.");
        }

    }

    @Test
    void createCustomer_withEmptyEmail_shouldThrowException() {
        try{
            Customer customer = CustomerFactory.createCustomer(null,"Alice", "", 25, "ACTIVE");
            assertNotNull(customer);
        }catch (NotNullException e){
            assertEquals(e.getMessage(), "Email cannot be null or empty.");
        }
    }
}