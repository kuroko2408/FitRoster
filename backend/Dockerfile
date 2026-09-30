FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY build/libs/fitroster-workout-api-0.1.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
