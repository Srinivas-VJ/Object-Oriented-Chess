# temp build image
FROM gradle:latest AS BUILD
WORKDIR /usr/app/
COPY . .
RUN gradle build

FROM openjdk
EXPOSE 8080
ADD build/libs/chess-0.0.1-SNAPSHOT.jar chess-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/chess-0.0.1-SNAPSHOT.jar"]