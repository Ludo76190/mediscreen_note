FROM openjdk:8-jdk-alpine
COPY target/*.jar msnote-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=docker","/msnote-0.0.1-SNAPSHOT.jar"]