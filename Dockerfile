FROM openjdk:17-jdk-alpine
MAINTAINER pricemonitoring
COPY target/taskpricemonitoringsystem-1.0-SNAPSHOT.war app.war
ENTRYPOINT ["java","-jar","/taskpricemonitoringsystem-1.0-SNAPSHOT.war"]