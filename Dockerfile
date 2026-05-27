# Etapa 1: Compilación del proyecto
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar el archivo pom.xml y descargar las dependencias (esto optimiza la caché de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar el archivo JAR (saltando los tests para acelerar el despliegue)
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final para ejecutar la aplicación
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiar el archivo .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Indicar el puerto en el que escucha la aplicación (Spring Boot usa el 8080 por defecto)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]