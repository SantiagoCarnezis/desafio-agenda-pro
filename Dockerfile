FROM openjdk:21-jdk
WORKDIR /app
COPY target/desafio-agenda-pro.jar app.jar
CMD ["java", "-jar", "app.jar"]
