FROM openjdk:8

WORKDIR /app

COPY ./target/helpmeiud-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 4343

CMD ["java", "-Dspring.profiles.active=default", "-jar", "app.jar"]