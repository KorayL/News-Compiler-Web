FROM eclipse-temurin:21-alpine

WORKDIR /app

# Install all dependencies
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

# Install curl for health check
RUN apk update && apk add --no-cache curl

# Copy the source code
COPY src ./src

# Build the application
CMD ["./mvnw", "spring-boot:run"]
