FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copiar la aplicación
COPY target/*.jar app.jar

EXPOSE 8084

# Iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]