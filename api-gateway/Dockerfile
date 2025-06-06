# 1) Etap build (Gradle Wrapper)
FROM gradle:8.4-jdk21 AS build

WORKDIR /home/gradle/project

COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle/ gradle/

COPY common common
COPY api-gateway api-gateway

RUN chmod +x gradlew

RUN ./gradlew :api-gateway:bootJar --no-daemon -x test


# 2) Etap produkcyjny
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /home/gradle/project/api-gateway/build/libs/api-gateway-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]