# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-focal AS build
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradlew.bat .
COPY build.gradle .
COPY settings.gradle .
COPY gradle ./gradle

# Copy source code
COPY src ./src

# Build the application
RUN ./gradlew bootJar

# Stage 2: Create the final image
FROM eclipse-temurin:17-jre-focal AS final
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
