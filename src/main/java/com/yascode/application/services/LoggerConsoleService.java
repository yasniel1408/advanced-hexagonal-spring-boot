package com.yascode.application.services;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

@NoArgsConstructor
public class LoggerConsoleService {
    private static final Logger logger = LogManager.getLogger(LoggerConsoleService.class);

    public void logWithBox(String message) {
        // Calcular el ancho dinámicamente basado en la línea más larga
        int maxLineLength = Arrays.stream(message.split("\n"))
                .mapToInt(String::length)
                .max()
                .orElse(0);

        // Calcular el ancho del recuadro (un poco más de la línea más larga)
        int boxWidth = Math.max(maxLineLength + 4, 50); // 4 = Espacios entre mensaje y bordes

        // Crear el borde superior e inferior
        String border = "+" + "-".repeat(boxWidth - 2) + "+";

        // Crear el contenido del recuadro
        StringBuilder box = new StringBuilder("\n").append(border).append("\n");

        // Dividir en líneas y dar formato
        for (String line : message.split("\n")) {
            box.append(formatLine(line, boxWidth)).append("\n");
        }

        // Agregar borde inferior
        box.append(border);

        // Imprimir el recuadro con Log4J
        logger.info(box.toString());
    }

    // Dar formato a cada línea con centrado
    private String formatLine(String line, int boxWidth) {
        // Espacios para centrado
        int padding = boxWidth - 4; // Longitud del mensaje entre los bordes "|   ...    |"
        int paddingLeft = (padding - line.length()) / 2;
        int paddingRight = padding - line.length() - paddingLeft;

        return "| " + " ".repeat(paddingLeft) + line + " ".repeat(paddingRight) + " |";
    }
}