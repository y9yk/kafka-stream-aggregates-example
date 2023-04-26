FROM maven:3.6.3-openjdk-8

MAINTAINER Yong Ki Lee "andrew.yk82@gmail.com"
ENV APP_HOME=/app

COPY pom.xml ${APP_HOME}/
COPY src ${APP_HOME}/src

WORKDIR ${APP_HOME}

RUN mvn clean package -DskipTests