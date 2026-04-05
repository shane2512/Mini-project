# Multi-stage Dockerfile for FTMS Backend
# Builds from the backend directory

# Build stage
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copy backend directory
COPY backend/pom.xml .
COPY backend/src ./src

# Build the application
RUN mvn dependency:go-offline -B && \
    mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/ftms-backend-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
