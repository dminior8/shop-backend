FROM gradle:8.4-jdk21 AS build

WORKDIR /home/gradle/project

COPY settings.gradle build.gradle gradlew gradle/ ./
COPY common common
COPY order-service order-service

RUN chmod +x gradlew
RUN ./gradlew :order-service:bootJar --no-daemon -x test

FROM openjdk:21-jdk-slim

WORKDIR /app
COPY --from=build /home/gradle/project/order-service/build/libs/order-service-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","app.jar"]