#Build Stage
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app

RUN mkdir -p /root/.m2 \ && mkdir /root/.m2/repository
COPY settings.xml /root/.m2

RUN mvn -f /usr/src/app/pom.xml clean package

#FROM gcr.io/distroless/java21-debian12
FROM eclipse-temurin:21-alpine
COPY --from=build /usr/src/app/target/quickstart-services-1.0.0.jar /usr/app/quickstart-services-1.0.0.jar
ENTRYPOINT ["java","-jar","/usr/app/quickstart-services-1.0.0.jar"]
