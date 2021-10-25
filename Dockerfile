FROM maven:3.6.1-jdk-11-slim AS build
COPY src /app/src
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean package

FROM openjdk:11-jre-slim
ARG JAR_FILE=/app/target/*.jar
COPY --from=build ${JAR_FILE} readingisgood.jar
ENTRYPOINT ["java","-jar","/readingisgood.jar"]
EXPOSE 8080