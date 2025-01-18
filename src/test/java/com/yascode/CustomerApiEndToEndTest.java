package com.yascode;

import com.yascode.infrastructure.in.http.request.CreateCustomerRequestDto;
import com.yascode.infrastructure.in.http.request.UpdateCustomerRequestDto;
import com.yascode.infrastructure.in.http.response.CustomerResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerApiEndToEndTest extends BaseTestContainer {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/v1/customers";
    }

    @Test
    void createCustomer() {
        // Crear un cliente
        CreateCustomerRequestDto createRequest = new CreateCustomerRequestDto("Marcelina Kerluke", 30, faker.internet().emailAddress(), "ACTIVE");

        ResponseEntity<CustomerResponseDto> createResponse = restTemplate.postForEntity(baseUrl(), createRequest, CustomerResponseDto.class);

        // Verificaciones
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertEquals(createRequest.name(), createResponse.getBody().name());
        assertEquals(createRequest.email(), createResponse.getBody().email());
    }

    @Test
    void getCustomers() {
        // Crear un cliente para la prueba
        CreateCustomerRequestDto createRequest = new CreateCustomerRequestDto("John Doe", 30, faker.internet().emailAddress(), "ACTIVE");
        restTemplate.postForEntity(baseUrl(), createRequest, CustomerResponseDto.class);

        // Obtener todos los clientes
        ResponseEntity<CustomerResponseDto[]> getAllResponse = restTemplate.getForEntity(baseUrl(), CustomerResponseDto[].class);

        // Verificaciones
        assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
        assertNotNull(getAllResponse.getBody());
        List<CustomerResponseDto> customers = Arrays.asList(getAllResponse.getBody());
        assertFalse(customers.isEmpty());
        assertEquals("John Doe", customers.get(0).name());
    }

    @Test
    void getCustomerById() {
        // Crear un cliente para la prueba
        CreateCustomerRequestDto createRequest = new CreateCustomerRequestDto("John Doe", 30, faker.internet().emailAddress(), "ACTIVE");
        CustomerResponseDto createdCustomer = restTemplate.postForEntity(baseUrl(), createRequest, CustomerResponseDto.class).getBody();
        assertNotNull(createdCustomer);

        // Obtener cliente por ID
        ResponseEntity<CustomerResponseDto> getByIdResponse = restTemplate.getForEntity(baseUrl() + "/" + createdCustomer.id(), CustomerResponseDto.class);

        // Verificaciones
        assertEquals(HttpStatus.OK, getByIdResponse.getStatusCode());
        assertNotNull(getByIdResponse.getBody());
        assertEquals("John Doe", getByIdResponse.getBody().name());
    }

    @Test
    void updateCustomer() {
        // Crear un cliente para la prueba
        CreateCustomerRequestDto createRequest = new CreateCustomerRequestDto("John Doe", 30, faker.internet().emailAddress(), "ACTIVE");
        CustomerResponseDto createdCustomer = restTemplate.postForEntity(baseUrl(), createRequest, CustomerResponseDto.class).getBody();
        assertNotNull(createdCustomer);

        // Actualizar el cliente
        UpdateCustomerRequestDto updateRequest = new UpdateCustomerRequestDto(createdCustomer.id(), "John Updated", 35, faker.internet().emailAddress(), "INACTIVE");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UpdateCustomerRequestDto> updateEntity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<CustomerResponseDto> updateResponse = restTemplate.exchange(baseUrl(), HttpMethod.PUT, updateEntity, CustomerResponseDto.class);

        // Verificaciones
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals("John Updated", updateResponse.getBody().name());
        assertEquals("INACTIVE", updateResponse.getBody().status());
    }

    @Test
    void deleteCustomer() {
        // Crear un cliente para la prueba
        CreateCustomerRequestDto createRequest = new CreateCustomerRequestDto("John Doe", 30, faker.internet().emailAddress(), "ACTIVE");
        CustomerResponseDto createdCustomer = restTemplate.postForEntity(baseUrl(), createRequest, CustomerResponseDto.class).getBody();
        assertNotNull(createdCustomer);

        // Eliminar el cliente
        restTemplate.delete(baseUrl() + "/" + createdCustomer.id());

        // Verificar que el cliente ya no existe
        ResponseEntity<CustomerResponseDto> afterDeleteResponse = restTemplate.getForEntity(baseUrl() + "/" + createdCustomer.id(), CustomerResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND, afterDeleteResponse.getStatusCode());
    }
}