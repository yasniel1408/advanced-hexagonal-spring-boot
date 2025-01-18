package com.yascode.application.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LoggerConsoleServiceTest {

    private LoggerConsoleService loggerConsoleService;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        loggerConsoleService = new LoggerConsoleService();

        // Configurar un stream para capturar la salida de consola
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void logWithBox() {
        // Mensaje de prueba
        String testMessage = "Hello, World!";

        // Llamar al método
        loggerConsoleService.logWithBox(testMessage);

        // Obtener la salida capturada
        String loggedOutput = outputStream.toString();

        // Verificar que la salida contiene el mensaje esperado
        assertTrue(loggedOutput.contains("+"), "Se esperaba que la salida contuviera bordes del recuadro");
        assertTrue(loggedOutput.contains("Hello, World!"), "Se esperaba que la salida incluyera el mensaje 'Hello, World!'");
    }
}