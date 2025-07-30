FROM eclipse-temurin:17-jre-jammy

# Instalar herramientas necesarias
RUN apt-get update && apt-get install -y \
    netcat-openbsd \
    curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copiar la aplicación
COPY target/*.jar app.jar

EXPOSE 8083

# Iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]