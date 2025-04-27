# 1) Etap build (Gradle Wrapper)
FROM gradle:8.4-jdk21 AS build

WORKDIR /home/gradle/project

COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle/ gradle/

COPY common common
COPY cart-service cart-service

RUN chmod +x gradlew

RUN ./gradlew :cart-service:bootJar --no-daemon -x test


# 2) Etap produkcyjny
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /home/gradle/project/cart-service/build/libs/cart-service-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]