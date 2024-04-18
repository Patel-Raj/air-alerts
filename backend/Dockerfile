FROM maven:3.9.6-amazoncorretto-17-al2023 AS builder
WORKDIR /app
COPY pom.xml ./
COPY src src/
RUN mvn clean package -DskipTests

FROM amazoncorretto:17
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

ENV AWS_ACCESS_KEY_ID=<PLACEHOLDER>
ENV AWS_SECRET_ACCESS_KEY=<PLACEHOLDER>
ENV AWS_SESSION_TOKEN=<PLACEHOLDER>

EXPOSE 8080
CMD ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]