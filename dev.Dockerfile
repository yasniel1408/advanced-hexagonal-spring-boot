# Usar una imagen base de OpenJDK
FROM openjdk:21-slim

# Instalar utilidades necesarias (como Maven si es necesario)
RUN apt-get update && apt-get install -y maven

# Crear un directorio de trabajo
WORKDIR /app

# Copiar solo los archivos de configuración
COPY pom.xml ./
COPY src ./src

# Instalar dependencias de Maven sin empaquetar el proyecto (optimización para desarrollo)
RUN mvn dependency:resolve

# Exponer el puerto correspondiente
EXPOSE 8080

# Comando para ejecutar la aplicación en modo desarrollo
CMD ["mvn", "spring-boot:run"]