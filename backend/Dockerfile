# 1. Build stage: compile Java app with Maven
FROM maven:latest AS build
WORKDIR .
COPY . .
RUN mvn clean package -DskipTests

# 2. Final stage: run with JRE, copy built jar
FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp

# Copy the jar from the build stage
COPY --from=build target/api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8181
ENTRYPOINT ["java", "-jar", "app.jar"]