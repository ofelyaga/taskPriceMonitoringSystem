Документация по сборке и развёртыванию приложения
1. Требования

Java 17+

Maven 3.8+

Docker 20.10+

Docker Compose 2.20+

2. Сборка приложения
   1. Клонируйте репозиторий:

git clone <ваш-репозиторий>
cd taskpricemonitoringsystem

   2. Соберите WAR-файл:

mvn clean package -DskipTests
Результат: target/taskpricemonitoringsystem-1.0-SNAPSHOT.war

3. Docker-образ приложения

Dockerfile:

FROM tomcat:latest
COPY target/taskpricemonitoringsystem-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/taskpricemonitoringsystem_war.war
EXPOSE 8080
CMD ["catalina.sh", "run"]

Сборка образа:

docker build -t myapp:latest .

4. Запуск с Docker Compose

docker-compose.yml:

services:
db:
image: postgres
restart: always
environment:
POSTGRES_PASSWORD: root
POSTGRES_USER: postgres
POSTGRES_DB: monitoring
ports:
- "5432:5432"

api:
image: myapp
depends_on:
- db
ports:
- "8080:8080"
environment:
DB_HOST: db
DB_PASSWORD: root
DB_USER: postgres
DB_NAME: monitoring
build:
dockerfile: ./Dockerfile
context: .

 
**Запуск:**

mvn clean compile package

docker-compose up --build

5. Конфигурация приложения

Настройки БД:

URL: jdbc:postgresql://db:5432/monitoring

Пользователь: postgres

Пароль: root

Переменные окружения:

spring.datasource.url=${DB_HOST}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

6. Пользовательские настройки Maven

Сборка WAR:

<packaging>war</packaging>
<plugin>
<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-war-plugin</artifactId>
<version>3.4.0</version>
</plugin>
