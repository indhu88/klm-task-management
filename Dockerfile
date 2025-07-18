# Stage 1: Build the application using Gradle
#Official OpenJDK maintained by the Adoptium project
#Only includes the Java Runtime (not compilers/tools)
#Lightweight Linux distro → fast, small image

FROM eclipse-temurin:17-jdk-alpine AS build

# Set the working directory
WORKDIR /app
# Echo message (optional)
RUN echo "Starting Docker build..."

# Copy build output (assuming you're using Gradle)
COPY build/libs/*.jar app.jar

# Expose application port (Spring Boot default is 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]