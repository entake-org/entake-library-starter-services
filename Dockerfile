#Build Stage
FROM docker.io/library/maven:3.9.12-eclipse-temurin-25-alpine AS build

#App Source
COPY generic-library-services/src /usr/src/app/src
COPY generic-library-services/pom.xml /usr/src/app

#Set Up For Build
RUN mkdir -p /root/.m2 && mkdir /root/.m2/repository
#COPY settings.xml /root/.m2

RUN mvn -f /usr/src/app/pom.xml clean package

#Assemble Container
FROM gcr.io/distroless/java25-debian13

COPY --from=build /usr/src/app/target/generic-library-services-1.0.0.jar /usr/app/services.jar
ENTRYPOINT ["java","-jar","/usr/app/services.jar"]
