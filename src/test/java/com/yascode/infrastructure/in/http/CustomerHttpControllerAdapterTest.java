package com.yascode.infrastructure.in.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yascode.application.usecases.*;
import com.yascode.infrastructure.in.http.request.CreateCustomerRequestDto;
import com.yascode.infrastructure.in.http.request.UpdateCustomerRequestDto;
import com.yascode.infrastructure.in.http.response.CustomerResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerHttpControllerAdapter.class)
class CustomerHttpControllerAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GetCustomersUseCase getCustomersUseCase;

    @MockitoBean
    private CreateCustomerUseCase createCustomerUseCase;

    @MockitoBean
    private GetCustomerByIdUseCase getCustomerByIdUseCase;

    @MockitoBean
    private DeleteCustomerByIdUseCase deleteCustomerByIdUseCase;

    @MockitoBean
    private UpdateCustomerUseCase updateCustomerUseCase;

    @BeforeEach
    void setUp() {
        Mockito.reset(getCustomersUseCase, createCustomerUseCase, getCustomerByIdUseCase, deleteCustomerByIdUseCase, updateCustomerUseCase);
    }

    @Test
    void getCustomers_returnsListOfCustomers() throws Exception {
        // Configurar respuesta simulada
        List<CustomerResponseDto> mockCustomers = Arrays.asList(
                new CustomerResponseDto(1, "John Doe", 30, "john.doe@example.com", "ACTIVE"),
                new CustomerResponseDto(2, "Jane Doe", 25, "jane.doe@example.com", "INACTIVE")
        );
        when(getCustomersUseCase.run()).thenReturn(mockCustomers);

        // Ejecutar solicitud GET
        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));

        // Verificar interacciones
        verify(getCustomersUseCase, times(1)).run();
    }

    @Test
    void createCustomer_createsAndReturnsCustomer() throws Exception {
        // Datos de entrada y salida simulados
        CreateCustomerRequestDto requestDto = new CreateCustomerRequestDto("John Doe", 30,"john.doe@example.com",  "ACTIVE");
        CustomerResponseDto responseDto = new CustomerResponseDto(1, "John Doe", 30, "john.doe@example.com", "ACTIVE");

        when(createCustomerUseCase.run(any(CreateCustomerRequestDto.class))).thenReturn(responseDto);

        // Ejecutar solicitud POST
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));

        // Verificar interacciones
        verify(createCustomerUseCase, times(1)).run(any(CreateCustomerRequestDto.class));
    }

    @Test
    void getById_returnsCustomer() throws Exception {
        // Configurar respuesta simulada
        CustomerResponseDto responseDto = new CustomerResponseDto(1, "John Doe", 30, "john.doe@example.com", "ACTIVE");
        when(getCustomerByIdUseCase.run(eq(1))).thenReturn(responseDto);

        // Ejecutar solicitud GET
        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));

        // Verificar interacciones
        verify(getCustomerByIdUseCase, times(1)).run(1);
    }

    @Test
    void deleteById_deletesCustomer() throws Exception {
        // Ejecutar solicitud DELETE
        mockMvc.perform(delete("/api/v1/customers/1"))
                .andExpect(status().isNoContent());

        // Verificar interacciones
        verify(deleteCustomerByIdUseCase, times(1)).run(1);
    }

    @Test
    void updateCustomer_updatesAndReturnsCustomer() throws Exception {
        // Datos de entrada y salida simulados
        UpdateCustomerRequestDto requestDto = new UpdateCustomerRequestDto(1, "John Doe", 30,"john.doe@example.com",  "ACTIVE");
        CustomerResponseDto responseDto = new CustomerResponseDto(1, "John Doe", 30, "john.doe@example.com", "ACTIVE");

        when(updateCustomerUseCase.run(any(UpdateCustomerRequestDto.class))).thenReturn(responseDto);

        // Ejecutar solicitud PUT
        mockMvc.perform(put("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));

        // Verificar interacciones
        verify(updateCustomerUseCase, times(1)).run(any(UpdateCustomerRequestDto.class));
    }
}