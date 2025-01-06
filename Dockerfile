# Usar una imagen base de OpenJDK
FROM openjdk:21-slim

# Configurar un directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR generado al contenedor
COPY target/mi-aplicacion.jar app.jar

# Exponer el puerto donde la aplicación escucha (cambiar si usas otro puerto)
EXPOSE 8080

# Definir el comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]