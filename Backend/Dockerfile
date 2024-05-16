FROM maven:3.8.5-openjdk-17

EXPOSE 8080

WORKDIR /Backend
COPY . .
RUN mvn clean install -DskipTests

CMD mvn spring-boot:run
