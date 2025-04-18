# syntax=docker/dockerfile:1

# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17-slim AS build

# Set the working directory
WORKDIR /app

# Add build argument to optionally skip tests
ARG SKIP_TESTS=false

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the application with unit tests (unless skipped)
RUN if [ "$SKIP_TESTS" = "true" ] ; then \
        echo "Skipping tests..." && \
        mvn clean package -DskipTests ; \
    else \
        echo "Running tests..." && \
        mvn clean package ; \
    fi

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built application from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 80

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
