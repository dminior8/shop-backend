FROM gradle:8.4-jdk21 AS build

WORKDIR /home/gradle/project

COPY product-service/gradle/ gradle/ settings.gradle build.gradle gradlew ./
COPY common common
COPY schema/schema.sql /docker-entrypoint-initdb.d/
COPY product-service product-service

RUN chmod +x gradlew
RUN gradle :product-service:bootJar --no-daemon -x test

FROM openjdk:21-jdk-slim

WORKDIR /app
COPY --from=build /home/gradle/project/product-service/build/libs/product-service-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","app.jar"]