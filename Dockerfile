# =========================
# BUILD STAGE
# =========================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy complete project
COPY . .

# Build all Maven modules, including common-lib, cloud and services
RUN mvn -B clean package -DskipTests

# =========================
# RUNTIME STAGE
# =========================
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Jar passed dynamically from Jenkins
ARG JAR_FILE

RUN test -n "$JAR_FILE"

COPY --from=build /app/${JAR_FILE} /app/app.jar

# Run app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]



