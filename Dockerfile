FROM tomcat:latest
COPY target/taskpricemonitoringsystem-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/taskpricemonitoringsystem_war.war
EXPOSE 8080
CMD ["catalina.sh", "run"]