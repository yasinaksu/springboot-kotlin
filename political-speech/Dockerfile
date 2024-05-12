FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY src ./src
COPY pom.xml .

RUN mvn clean install -DskipTests

FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/target/political-speech-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
