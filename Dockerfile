# Build stage
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /dashboard
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app
COPY --from=build /dashboard/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
