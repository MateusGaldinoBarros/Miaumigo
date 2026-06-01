FROM node:22-alpine AS frontend-build

WORKDIR /app/frontend

COPY frontend/package*.json ./

RUN npm ci

COPY frontend ./

RUN npm run build

FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN rm -rf ./src/main/resources/static/*

COPY --from=frontend-build /app/frontend/dist/ ./src/main/resources/static/

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre AS execute

WORKDIR /app

COPY --from=build /app/target/*.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
