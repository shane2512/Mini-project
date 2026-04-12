# Dockerfile packages your Spring Boot application for deployment on Render.
# It defines the environment your app runs in: Java version, build steps, run command.

# Stage 1: Build Stage
# Use Maven with Java 17 to build the project
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set working directory inside the container
WORKDIR /app

# Copy pom.xml first (Docker caches this layer if pom.xml didn't change, making rebuilds faster)
COPY pom.xml .

# Download all dependencies (cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# Copy all source code
COPY src ./src

# Build the JAR file, skipping tests for faster build
RUN mvn clean package -DskipTests

# Stage 2: Run Stage  
# Use a smaller JRE image just to run the JAR (no Maven needed at runtime)
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/ftms-backend.jar app.jar

# Expose port 8080 so Render knows this app listens on 8080
EXPOSE 8080

# Command to run the application
# Environment variables like SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME,
# SPRING_DATASOURCE_PASSWORD, and JWT_SECRET are set in Render's dashboard.
ENTRYPOINT ["java", "-jar", "app.jar"]
