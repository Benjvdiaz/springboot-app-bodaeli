# Etapa 1: Compilación
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar el pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el resto del código y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen ligera para producción
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copiar el jar generado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto 8080
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]